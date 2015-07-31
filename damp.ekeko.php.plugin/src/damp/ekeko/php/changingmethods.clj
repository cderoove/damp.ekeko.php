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
  [method methodinvocation2callerandcallee]
  (let [callee2caller (:callee2caller methodinvocation2callerandcallee)]
    (count (distinct (get callee2caller method)))))
  
  
                         
(defn 
  changingmethods
  []
  (def methodinvocation2callerandcallee(metrics/methodinvocation2callerandcallee))
  (reduce (fn [mapsofar method] (assoc mapsofar method (changingmethodsmethod method methodinvocation2callerandcallee)))
             {} 
             (astnode/methoddeclarations))) 



                           