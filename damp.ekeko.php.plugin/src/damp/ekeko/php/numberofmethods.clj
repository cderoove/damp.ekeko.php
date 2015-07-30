(ns damp.ekeko.php.numberofmethods
 (:require 
    [damp.ekeko.php
     [astnode :as astnode]]))

(defn
  numberofmethodsclass
  [class]
  (count (astnode/nodeorvalue-offspring|type class :MethodDeclaration)))



(defn numberofmethods
  []  
  (reduce (fn [mapsofar class] (assoc mapsofar class (numberofmethodsclass class)))
             {} 
             (astnode/classdeclarations)))
