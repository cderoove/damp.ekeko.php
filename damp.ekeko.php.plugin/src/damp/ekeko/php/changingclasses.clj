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

(def not-nil? (complement nil?))


(defn 
  changingclassesmethod
  [method]
  (let [methodinvocations (astnode/asts-for-keyword :MethodInvocation)]
       (loop [methodinvocations methodinvocations
                               CC []]
                           (if (empty? methodinvocations)
                             (count (distinct CC))
                             (let [methodinvocation (first methodinvocations)
                                    node (metrics/methodinvocating methodinvocation)]
                               (if (= node method)
                                 (let [invocatingmethod (astnode/node-firstancestor|type methodinvocation :MethodDeclaration)
                                       decnode (metrics/declaringnode methodinvocation)]
                                  (if (not-nil? decnode)
                                  (recur (rest methodinvocations)
                                         (conj CC decnode))
                                  (recur (rest methodinvocations)
                                         CC)))
                                 (recur (rest methodinvocations)
                                         CC)))))))

(defn 
  changingclasses
  []
  (reduce (fn [mapsofar method] (assoc mapsofar method (changingclassesmethod method)))
             {} 
             (astnode/methoddeclarations)))
