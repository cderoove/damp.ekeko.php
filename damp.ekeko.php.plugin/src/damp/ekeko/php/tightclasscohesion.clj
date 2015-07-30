(ns damp.ekeko.php.tightclasscohesion
   (:require 
     [damp.ekeko.php
     [astnode :as astnode]]
      [damp.ekeko.php
     [metrics :as metrics]]
     [damp.ekeko.php
     [astbindings :as astbinding]]
    [damp.ekeko.php
     [weightedmethodcount :as WMC]] 
    [damp.ekeko.php
     [aststructure :as aststruct]]
    [damp.ekeko.php
     [numberofmethods :as NOM ]]
    [clojure.set]))

(defn 
 numberofpossibleconnections
 [class]
 (let [numberofmethods (NOM/numberofmethodsclass class)]
    (/ (* numberofmethods (- numberofmethods 1)) 2 )))

                         
(defn 
  numberofmethodinvocations
  [class]
  (let [methodinvocations  (astnode/nodeorvalue-offspring|type class :MethodInvocation)]
  (loop [methodinvocations methodinvocations
         MI []]
           (if (empty? methodinvocations)
             (count MI)
             (let [methodinvocation (first methodinvocations)
                    decnode (metrics/declaringnode methodinvocation)]
               (pr "decnode " decnode)
               (pr "class" class)
                 (if (= decnode class)
                   (recur (rest methodinvocations)
                          (conj MI methodinvocation))
                   (recur (rest methodinvocations)
                           MI)))))))


(defn 
  fieldaccessesintersection
  [fieldbindingsothermethods fieldbindingsmethod]
(loop [fieldbindingsothermethods  fieldbindingsothermethods
       DFAM 0]
  (if (empty? fieldbindingsothermethods)
    DFAM
  (let [firstotherbindings (first fieldbindingsothermethods)]
    (recur (rest fieldbindingsothermethods)
      (+ DFAM (count (clojure.set/intersection (set (val firstotherbindings)) (set (val fieldbindingsmethod))))))))))


  
(defn 
  numberofdirectfieldaccesses 
  [class]
 (let [fieldaccessesmethods (astnode/fieldaccessesmethods class)
       fieldbindingsmethods (astbinding/fieldbindingsmethods fieldaccessesmethods)]
   (loop [fieldbindingsmethods fieldbindingsmethods
          DFA 0]
     (if (empty? fieldbindingsmethods)
       DFA
       (let [fieldbingsmethod (first fieldbindingsmethods)] 
         (recur (rest fieldbindingsmethods)
                (+ DFA (fieldaccessesintersection  (rest fieldbindingsmethods) fieldbingsmethod))))))))

  
(defn 
  numberofdirectconnections
  [class]
  ( + (numberofdirectfieldaccesses class) ( numberofmethodinvocations class)))
  
(defn
  tightclasscohesionclass
  [class]
  (/ (numberofdirectconnections class) (if ( < (numberofpossibleconnections class) 1)
                                         1
                                         (numberofpossibleconnections class))))

(defn 
  tightclasscohesion
  []
  (reduce (fn [mapsofar class] (assoc mapsofar class (tightclasscohesionclass class)))
             {} 
             (astnode/classdeclarations)))