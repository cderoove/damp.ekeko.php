(ns damp.ekeko.php.metrics
  (:require 
    [damp.util.interop]
     [damp.ekeko.php
     [bindings :as bindings]]
     [damp.ekeko.php
     [astbindings :as astbindings]]
     [damp.ekeko.php
     [aststructure :as aststruct]]
     [damp.ekeko.php
     [astnode :as astnode]]))


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
 
 

(defn
  methodinvocation2callerandcallee
  []
  (let [methodinvocations (astnode/asts-for-keyword :MethodInvocation)]
       (loop [methodinvocations methodinvocations
                               methodinvocation2callee {}
                               methodinvocation2caller {}
                                methodinvocation2decnode{}
                                callee2caller {}
                                callee2decnode {}]
                           (if (empty? methodinvocations)
                            {:callee2decnode callee2decnode, :callee2caller callee2caller :methodinvocation2callee methodinvocation2callee, :methodinvocation2caller methodinvocation2caller, :methodinvocation2class methodinvocation2decnode}
                             (let [methodinvocation (first methodinvocations)
                                    callee (methodinvocating methodinvocation)
                                    caller (astnode/node-firstancestor|type methodinvocation :MethodDeclaration)
                                     decnode (declaringnode methodinvocation)]
                               (recur (rest methodinvocations)
                                      (assoc methodinvocation2callee methodinvocation callee)
                                      (assoc methodinvocation2caller methodinvocation caller)
                                      (assoc methodinvocation2decnode methodinvocation decnode)
                                      (merge-with concat callee2caller { callee [caller]})
                                      (merge-with concat callee2decnode { callee [decnode ]})))))))
                                
 



