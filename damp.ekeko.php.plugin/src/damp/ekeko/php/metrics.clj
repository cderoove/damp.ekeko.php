(ns damp.ekeko.php.metrics
  (:require 
    [damp.util.interop]
     [damp.ekeko.php
     [bindings :as bindings]]
     [damp.ekeko.php
     [astbindings :as astbindings]]
     [damp.ekeko.php
     [aststructure :as aststruct]]))


  (defn 
    declaringnode
    [ast]
    (if-let [binding (astbindings/ast|correctbinding ast)]
                        (if-let [declaringbinding (bindings/binding|declaringclass binding)]
                          (if-let [typedecelement (bindings/binding|element declaringbinding)]
                             (if-let [decnode (aststruct/declaration-for-element typedecelement)]
                              decnode
                              nil)))))


 (defn
   methodinvocating
   [methodinvocation]
 (if-let [methodbinding (astbindings/ast|methodinvocation-binding|method methodinvocation)]
                           (if-let [typelement (bindings/binding|element methodbinding)]
                              (if-let [node (aststruct/declaration-for-element typelement)]
                               node
                               nil))))
 
   
 
 (defn mapintersection
   [map1 map2]
   (clojure.set/intersection (set map1) (set map2)))
