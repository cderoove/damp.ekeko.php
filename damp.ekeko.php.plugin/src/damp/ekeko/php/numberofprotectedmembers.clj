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
    [org.eclipse.php.internal.core.ast.nodes.BodyDeclaration Modifiers]
    [org.eclipse.php.internal.core.ast.nodes 
     BodyDeclaration ASTNode StructuralPropertyDescriptor ChildListPropertyDescriptor ChildPropertyDescriptor SimplePropertyDescriptor]))


(defn
  numberofprotecedmembersclass
  [class]
  (let [ methods (astnode/nodeorvalue-offspring|type class :MethodDeclaration)]
  (loop [ methods methods
         NProtM []]
    (if (empty? methods)
      (count NProtM)
      (let [method (first methods)
            modifier (astnode/modifier method)]
       (if (Modifiers/isPrivate modifier)
          (recur (rest methods)
                (conj NProtM method))
          (recur (rest methods)
                 NProtM)))))))




