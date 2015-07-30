(ns damp.ekeko.php.changingmethods
   (:require 
    [damp.util.interop]
     [damp.ekeko.php
     [metrics :as metrics]]
     [damp.ekeko.php
     [astnode :as astnode]]
    [damp.ekeko.php
     [aststructure :as aststruct]] ))

(defn 
  changingmethodsmethod
  [method]
  (let [methodinvocations (astnode/asts-for-keyword :MethodInvocation)]
       (loop [methodinvocations methodinvocations
                               CM []]
                           (if (empty? methodinvocations)
                             (count (distinct CM))
                             (let [methodinvocation (first methodinvocations)
                                    node (metrics/methodinvocating methodinvocation)]
                               (if (= node  method)
                                 (let [invocatingmethod (astnode/node-firstancestor|type methodinvocation :MethodDeclaration)]
                                  (recur (rest methodinvocations)
                                         (conj CM invocatingmethod)))
                                 (recur (rest methodinvocations)
                                        CM)))))))
(defn 
  changingmethods
  []
  (reduce (fn [mapsofar method] (assoc mapsofar method (changingmethodsmethod method)))
             {} 
             (astnode/methoddeclarations)))



                           