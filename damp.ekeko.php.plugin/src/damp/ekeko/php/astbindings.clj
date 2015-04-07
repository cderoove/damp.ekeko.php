(ns 
  damp.ekeko.php.astbindings
  (:refer-clojure :exclude [== type])
  (:require [clojure.core [logic :as cl]])
  (:require 
    [damp.ekeko [logic :as el]])
  (:require
    [damp.ekeko.php [ast :as ast] [bindings :as bindings] [phpprojectmodel :as phprojectmodel]])
  (:import 
    [org.eclipse.php.internal.core.ast.nodes Identifier Include Variable 
     TypeDeclaration StaticMethodInvocation StaticFieldAccess MethodInvocation MethodDeclaration FunctionInvocation FunctionDeclaration
     FormalParameter FieldsDeclaration FieldAccess Expression ClassInstanceCreation StaticConstantAccess NamespaceName Program])
  )



(defn
  ast|identifier-binding
  [?ast ?binding]
  (cl/all 
    (ast/ast|identifier ?ast)
    (cl/!= nil ?binding)
    (el/equals ?binding (.resolveBinding ^Identifier ?ast))))


(defn
  ast|namespacename-binding
  [?ast ?binding]
  (cl/all 
    (ast/ast|namespacename ?ast)
    (cl/!= nil ?binding)
    (el/equals ?binding (.resolveBinding ^NamespaceName ?ast))))


(defn
  ast|include-binding
  [?ast ?binding]
  (cl/all 
    (ast/ast|include ?ast)
    (cl/!= nil ?binding)
    (el/equals ?binding (.resolveBinding ^Include ?ast))))


(defn
  ast|variable-binding
  [?ast ?binding]
  (cl/all 
    (ast/ast|variable ?ast)
    (cl/!= nil ?binding)
    (el/equals ?binding (.resolveVariableBinding ^Variable ?ast))))

(defn
  ast|typedeclaration-binding|type
  [?ast ?binding]
  (cl/all 
    (ast/ast|typedeclaration ?ast)
    (cl/!= nil ?binding)
    (el/equals ?binding (.resolveTypeBinding ^TypeDeclaration ?ast))))


(defn
  ast|staticmethodinvocation-binding
  [?ast ?binding]
  (cl/all 
    (ast/ast|staticmethodinvocation ?ast)
    (cl/!= nil ?binding)
    (el/equals ?binding (.resolveMethodBinding ^StaticMethodInvocation ?ast))))

(defn
  ast|methodinvocation-binding|method
  [?ast ?binding]
  (cl/all 
    (ast/ast|methodinvocation ?ast)
    (cl/!= nil ?binding)
    (el/equals ?binding (.resolveMethodBinding ^MethodInvocation ?ast))))


(defn
  ast|methodinvocation-binding|constructor
  [?ast ?binding]
  (cl/all 
    (ast/ast|methodinvocation ?ast)
    (cl/!= nil ?binding)
    (el/equals ?binding (.resolveConstructorBinding ^MethodInvocation ?ast))))

(defn
  ast|staticfieldaccess-binding
  [?ast ?binding]
  (cl/all 
    (ast/ast|staticfieldaccess ?ast)
    (cl/!= nil ?binding)
    (el/equals ?binding (.resolveFieldBinding ^StaticFieldAccess ?ast))))

(defn
  ast|fieldaccess-binding
  [?ast ?binding]
  (cl/all 
    (ast/ast|fieldaccess ?ast)
    (cl/!= nil ?binding)
    (el/equals ?binding (.resolveFieldBinding ^FieldAccess ?ast))))


(defn
  ast|staticconstantaccess-binding
  [?ast ?binding]
  (cl/all 
    (ast/ast|staticconstantaccess ?ast)
    (cl/!= nil ?binding)
    (el/equals ?binding (.resolveFieldBinding ^StaticConstantAccess ?ast))))


(defn
  ast|methoddeclaration-binding
  [?ast ?binding]
  (cl/all 
    (ast/ast|methoddeclaration ?ast)
    (cl/!= nil ?binding)
    (el/equals ?binding (.resolveMethodBinding ^MethodDeclaration ?ast))))


(defn
  ast|functioninvocation-binding
  [?ast ?binding]
  (cl/all 
    (ast/ast|functioninvocation ?ast)
    (cl/!= nil ?binding)
    (el/equals ?binding (.resolveFunctionBinding ^FunctionInvocation ?ast))))


(defn
  ast|functiondeclaration-binding
  [?ast ?binding]
  (cl/all 
    (ast/ast|functiondeclaration ?ast)
    (cl/!= nil ?binding)
    (el/equals ?binding (.resolveFunctionBinding ^FunctionInvocation ?ast))))

(defn
  ast|fieldsdeclaration-binding|type
  [?ast ?binding]
  (cl/all 
    (ast/ast|functiondeclaration ?ast)
    (cl/!= nil ?binding)
    (el/equals ?binding (.resolveTypeBinding ^FunctionInvocation ?ast))))

(defn
  ast|expression-binding|type
  [?keyw ?ast ?binding]
  (cl/all
    (ast/ast|expression ?keyw ?ast)
    (cl/!= nil ?binding)
    (el/equals ?binding (.resolveTypeBinding ^Expression ?ast))))
  
(defn
  ast|classinstancecreation-binding|constructor
  [?ast ?binding]
  (cl/all 
    (ast/ast|classinstancecreation ?ast)
    (cl/!= nil ?binding)
    (el/equals ?binding (.resolveConstructorBinding ^ClassInstanceCreation ?ast))))



  