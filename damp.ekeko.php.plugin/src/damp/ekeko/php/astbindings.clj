(ns 
  damp.ekeko.php.astbindings
  (:refer-clojure :exclude [== type])
  (:require [clojure.core [logic :as cl]])
  (:require 
    [damp.ekeko [logic :as el]])
  (:require
    [damp.ekeko.php  [bindings :as bindings]])
  (:import 
    [org.eclipse.php.internal.core.ast.nodes Identifier Include Variable 
     TypeDeclaration StaticMethodInvocation StaticFieldAccess MethodInvocation MethodDeclaration FunctionInvocation FunctionDeclaration
     FormalParameter FieldsDeclaration FieldAccess Expression ClassInstanceCreation StaticConstantAccess NamespaceName Program])
  )



(defn
  ast|binding
  [ast]
  (.resolveBinding  ast))


(defn
  ast|variable-binding
  [ast]
  (.resolveVariableBinding ast))

(defn
  ast|typedeclaration-binding|type
  [ast]
  (.resolveTypeBinding ast))



(defn
  ast|methodinvocation-binding|method
  [ast]
  (.resolveMethodBinding ast))


(defn
  ast|methodinvocation-binding|constructor
  [ast]
  (.resolveConstructorBinding ast))


(defn
  ast|fieldaccess-binding
  [ast]
(try
   (.resolveFieldBinding ast)
   (catch NullPointerException e
       nil )))

(defn
  ast|methoddeclaration-binding
  [ast]
  (.resolveMethodBinding ast))


(defn
  ast|functioninvocation-binding
  [ast]
  (.resolveFunctionBinding ast))


(defn 
  fieldbindingsmethods
  [method2fieldacceslist]
  (reduce
    (fn [newmapsofar [method fieldaccesslist]]
      (assoc newmapsofar method (map (fn [access] 
                                       (try 
                                         (.resolveFieldBinding access)
                                         (catch Exception e nil)))
                                     fieldaccesslist)))
    {}
    method2fieldacceslist))
    
   

  
(defn
  ast|correctbinding
  [ast]
  (if  
    (or (instance? FieldAccess  ast) (instance? StaticConstantAccess ast) (instance? StaticFieldAccess ast))
     (ast|fieldaccess-binding ast)
     (if
       (or (instance? MethodDeclaration ast) (instance? MethodInvocation ast))
       (ast|methoddeclaration-binding ast)
       (ast|typedeclaration-binding|type ast))))
  

  