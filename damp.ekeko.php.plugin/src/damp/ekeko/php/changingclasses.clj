(ns damp.ekeko.php.changingclasses
   (:require 
    [damp.util.interop]
     [damp.ekeko.php
     [astnode :as astnode]]
      [damp.ekeko.php
     [metrics :as metrics]]
     [damp.ekeko.php
     [astbindings :as astbinding]]
      [damp.ekeko.php
     [bindings :as bindings]]
    [damp.ekeko.php
     [aststructure :as aststruct]] ))

(defn
  changingclassesmethod
  [method methodinvocation2callerandcallee]
  (let [callee2decnode (:callee2decnode methodinvocation2callerandcallee)]
    (count (distinct (get callee2decnode method)))))
  

                        
(defn 
  changingclasses
  []
  (def methodinvocation2callerandcallee (metrics/methodinvocation2callerandcallee))
  (reduce (fn [mapsofar method] (assoc mapsofar method (changingclassesmethod method  methodinvocation2callerandcallee)))
             {} 
             (astnode/methoddeclarations)))
