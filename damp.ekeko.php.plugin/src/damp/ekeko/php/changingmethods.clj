(ns damp.ekeko.php.changemethod
   (:require 
    [damp.util.interop]
     [damp.ekeko.php
     [astnode :as astnode]]
    [damp.ekeko.php
     [aststructure :as aststruct]] ))

(defn 
  changemethodmethod
  [method]
  (let [methodinvocations (astnode/asts-for-keyword :MethodInvocation)]
       (loop [methodinvocations methodinvocations
                               CM []]
                           (if (empty? methodinvocations)
                             (count CM)
                             (let [methodinvocation (first methodinvocations)
                                    node (if-let [methodbinding (.resolveMethodBinding methodinvocation)]
                                             (if-let [typelement (.getPHPElement methodbinding)]
                                                (if-let [node (aststruct/declaration-for-element typelement)]
                                                 node
                                                 nil)))]
                               (if (= node method)
                                 (let [invocatingmethod (astnode/nodeo-ancestors|type methodinvocation :MethodDeclaration)]
                                  (recur (rest methodinvocations)
                                         (conj CM invocatingmethod)))
                                 (recur (rest methodinvocations)
                                        CM)))))))


(defn 
  changemethod
  []
  (reduce (fn [mapsofar method] (assoc mapsofar method (changemethodmethod method)))
             {} 
             (astnode/asts-for-keyword :MethodDeclaration)))
                           