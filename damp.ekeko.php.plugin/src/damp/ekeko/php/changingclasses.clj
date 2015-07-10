(ns damp.ekeko.php.changingclasses
   (:require 
    [damp.util.interop]
     [damp.ekeko.php
     [astnode :as astnode]]
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
                             (count CC)
                             (let [methodinvocation (first methodinvocations)
                                    node (if-let [methodbinding (.resolveMethodBinding methodinvocation)]
                                             (if-let [typelement (.getPHPElement methodbinding)]
                                                (if-let [node (aststruct/declaration-for-element typelement)]
                                                 node
                                                 nil)))]
                               (if (= node method)
                                 (let [invocatingmethod (astnode/nodeo-ancestors|type methodinvocation :MethodDeclaration)
                                       decnode (if-let [invocmethodbinding (.resolveMethodBinding invocatingmethod)]
                                                 (if-let [declaringbinding (.getDeclaringClass invocmethodbinding)]
                                                   (if-let [typedecelement (.getPHPElement declaringbinding)]
                                                      (if-let [decnode (aststruct/declaration-for-element typedecelement)]
                                                       decnode
                                                       nil))))]
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
             (astnode/asts-for-keyword :MethodDeclaration)))
