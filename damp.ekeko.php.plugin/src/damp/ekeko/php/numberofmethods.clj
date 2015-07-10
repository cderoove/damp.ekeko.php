(ns damp.ekeko.php.numberofmethods
 (:require 
    [damp.util.interop]
    [damp.ekeko.php
     [astnode :as astnode]] ;todo: extract commonalities to shared namespace
    [damp.ekeko.php
     [phpprojectmodel :as pm]]
    [damp.ekeko.php
     [aststructure :as aststruct]])
  (:import 
    [org.eclipse.php.internal.core.ast.nodes 
     ASTNode StructuralPropertyDescriptor ChildListPropertyDescriptor ChildPropertyDescriptor SimplePropertyDescriptor]))

(defn
  numberofmethodsclass
  [class]
  (count (astnode/nodeorvalue-offspring|type class :MethodDeclaration)))



(defn numberofmethods
  []  
  (reduce (fn [mapsofar class] (assoc mapsofar class (numberofmethodsClass class)))
             {} 
             (astnode/asts-for-keyword :ClassDeclaration)))
