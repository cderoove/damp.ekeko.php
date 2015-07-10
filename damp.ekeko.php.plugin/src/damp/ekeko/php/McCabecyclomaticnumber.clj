(ns damp.ekeko.php.McCabecyclomaticnumber
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
CYCLO
[method]
(let [numberoffifstatements (count (astnode/nodeorvalue-offspring|type method :IfStatement))
      numberofswitchCases (count (astnode/nodeorvalue-offspring|type  method :SwitchCase))
      numberofforeachtstatements (count (astnode/nodeorvalue-offspring|type  method  :ForEachStatement))
      numberofforstatements (count (astnode/nodeorvalue-offspring|type  method :ForStatement))
      numberofcatchclauses (count (astnode/nodeorvalue-offspring|type  method :CatchClause))
      numberofwhilestatements (count (astnode/nodeorvalue-offspring|type  method :WhileStatement))
      numberofdecisions (+ numberoffifstatements numberofswitchCases numberofforeachtstatements numberofforstatements numberofcatchclauses numberofwhilestatements)
      CYCLO (+ numberofdecisions 1)]
  CYCLO))
