(ns damp.ekeko.php.weightedmethodcount
   (:require 
    [damp.ekeko.php
     [astnode :as astnode]] 
    [damp.ekeko.php
     [McCabecyclomaticnumber :as McCabe]]))

(defn 
  weightedmethodcountclass
  [class]
  (let [methods (astnode/nodeorvalue-offspring|type class :MethodDeclaration)]
  (apply + (map (fn [method] (McCabe/CYCLO method))
            methods))))    
             
             
(defn weightedmethodcount
  []  
  (reduce (fn [mapsofar class] (assoc mapsofar class (weightedmethodcountclass class)))
             {} 
             (astnode/classdeclarations)))
                 
(defn weightedmethodcountname
  []  
  (reduce (fn [mapsofar class] (assoc mapsofar class (.getBinaryName (.resolveTypeBinding class))))
             {} 
             (astnode/classdeclarations)))
                      