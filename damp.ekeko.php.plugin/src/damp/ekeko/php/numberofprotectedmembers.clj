(ns damp.ekeko.php.numberofprotectedmembers
 (:require 
    [damp.util.interop]
    [damp.ekeko.php
     [astnode :as astnode]] ;todo: extract commonalities to shared namespace
    [damp.ekeko.php
     [phpprojectmodel :as pm]]
    [damp.ekeko.php
     [aststructure :as aststruct]]
    [damp.ekeko.php
     [astbindings :as astbindings]])
  (:import
    [org.eclipse.dltk.ast Modifiers] 
    [org.eclipse.php.internal.core.ast.nodes BodyDeclaration$Modifier]
    [org.eclipse.php.internal.core.ast.nodes 
     BodyDeclaration ASTNode StructuralPropertyDescriptor ChildListPropertyDescriptor ChildPropertyDescriptor SimplePropertyDescriptor]))

    
 (defn 
   numberofprotectedmethods
  [class]
  (let [ methods (astnode/nodeorvalue-offspring|type class :MethodDeclaration)]
  (loop [ methods methods
         NProtM []]
    (if (empty? methods)
      (count NProtM)
      (let [method (first methods)
            modifier (astnode/modifier method)]
       (if (= (Modifiers/AccProtected) modifier)
          (recur (rest methods)
                (conj NProtM method))
          (recur (rest methods)
                 NProtM)))))))
 
 

(defn
  numberofprotectedfields
  [class]
  (let [fields (astnode/nodeorvalue-offspring|type class :FieldsDeclaration)]
  (loop [fields fields
         NprotF []]
    (if (empty? fields)
      (count NprotF)
      (let [field (first fields)
            modifier (astnode/modifier field)]
       (if (= (Modifiers/AccProtected) modifier)
          (recur (rest fields)
                (conj NprotF fields))
          (recur (rest fields)
                 NprotF)))))))
 
 (defn
 numberofprotecedmembersclass
 [class]
 ( + (numberofprotectedfields class) (numberofprotectedmethods class)))