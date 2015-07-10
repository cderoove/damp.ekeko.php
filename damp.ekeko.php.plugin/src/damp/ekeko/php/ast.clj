(ns 
  damp.ekeko.php.ast
  (:refer-clojure :exclude [== type])
  (:require 
    [clojure.core
     [logic :as cl]]
    [damp.util.interop]
    [damp.ekeko
     [logic :as el]]
    [damp.ekeko.php
     [astnode :as astnode]])
  (:import 
    [org.eclipse.jdt.core.dom Expression]))

;; Logic relations

(defn 
  ast
  "See Ekeko's ast/2"
  [?keyword ?node]
  (cl/conda [(el/v+ ?node) 
             (cl/conda [(el/v+ ?keyword) 
                        (el/succeeds-without-exception (astnode/ast-of-keyword? ?node ?keyword))]
                       [(el/v- ?keyword)
                        (el/succeeds (astnode/ast? ?node))
                        (el/equals ?keyword (astnode/keyword-for-class-of ?node))])]
            [(el/v- ?node) 
             (cl/conda [(el/v+ ?keyword)
                        (cl/fresh [?nodes]
                                  (el/equals ?nodes (astnode/asts-for-keyword ?keyword))
                                  (el/contains ?nodes ?node))]
                       [(el/v- ?keyword)
                        (cl/fresh [?nodes]
                                  (el/equals ?nodes (astnode/asts))
                                  (el/contains ?nodes ?node)
                                  (el/equals ?keyword (astnode/keyword-for-class-of ?node)))])]))
             

(defn
  has
  "See Ekeko's has/3"
  [?keyword ?node ?child]
  (cl/conda [(el/v+ ?node) 
             (el/succeeds (astnode/ast? ?node))
             (cl/conda [(el/v+ ?keyword) 
                        (cl/fresh [?childretrievingf]
                                 (el/equals ?childretrievingf (?keyword (astnode/node-ekeko-properties ?node)))
                                 (cl/!= ?childretrievingf nil)
                                 (el/equals ?child (?childretrievingf ?node)))]
                       [(el/v- ?keyword)
                        (cl/fresh [?keywords]
                                  (el/equals ?keywords (keys (astnode/node-ekeko-properties ?node)))
                                  (el/contains ?keywords ?keyword)
                                  (has ?keyword ?node ?child))])]
            [(el/v- ?node)
             (cl/conda [(el/v+ ?child)
                        (cl/all (el/equals ?node (astnode/owner ?child)) 
                                (has ?keyword ?node ?child))]
                       [(el/v- ?child)
                        (cl/fresh [?astkeyw]
                                 (ast ?astkeyw ?node)
                                 (has ?keyword ?node ?child))])]))
                        
(defn
  value
  "See Ekeko's value/1."
  [?val]
  (cl/conda
    [(el/v+ ?val)
     (el/succeeds (astnode/value? ?val))]
    [(el/v- ?val)
     (cl/fresh [?kind ?ast ?property]
              (ast ?kind ?ast)
              (has ?property ?ast ?val)
              (value ?val))]))

(defn
  value|null
  "See Ekeko's value|null/1."
  [?val]
  (cl/all
    (value ?val)
    (el/succeeds (astnode/value|null? ?val))))

(defn
  value|list
  "See Ekeko's value|list/1"
  [?val]
  (cl/all
    (value ?val)
    (el/succeeds (astnode/value|list? ?val))))


(defn
  value|primitive 
  "See Ekeko's value|primitive/1."
  [?val]
  (cl/all
    (value ?val)
    (el/succeeds (astnode/value|primitive? ?val))))


(defn
  value-raw
  "See Ekeko's value-raw/2"
  [?val ?raw]
  (cl/all
    (value ?val)
    (el/equals ?raw (astnode/value-unwrapped ?val))))



(defn 
  ast|identifier
  [?ast]
  (cl/all 
    (ast :Identifier ?ast)))

(defn 
  ast|namespacename
  [?ast]
  (cl/all 
    (ast :NamespaceName ?ast)))


(defn 
  ast|include
  [?ast]
  (cl/all 
    (ast :Include ?ast)))

(defn 
  ast|fieldaccess
  [?ast]
  (cl/all 
    (ast :FieldAccess ?ast)))


(defn 
  ast|variable
  [?ast]
  (cl/all 
    (ast :Variable ?ast)))

(defn 
  ast|typedeclaration
  [?ast]
  (cl/conde
    [(ast :ClassDeclaration ?ast)]
     
    [(ast :TraitDeclaration ?ast)]
    [(ast :InterfaceDeclaration ?ast)]))

(defn 
  ast|staticmethodinvocation
  [?ast]
  (cl/all 
    (ast :StaticMethodInvocation ?ast)))

(defn 
  ast|staticconstantaccess
  [?ast]
  (cl/all 
    (ast :StaticConstantAccess ?ast)))

(defn 
  ast|staticfieldaccess
  [?ast]
  (cl/all 
    (ast :StaticFieldAccess ?ast)))

(defn 
  ast|methodinvocation
  [?ast]
  (cl/all 
    (ast :MethodInvocation ?ast)))

(defn 
  ast|methoddeclaration
  [?ast]
  (cl/all 
    (ast :MethodDeclaration ?ast)))

(defn 
  ast|functioninvocation
  [?ast]
  (cl/all 
    (ast :FunctionInvocation ?ast)))
  
(defn 
  ast|functiondeclaration
  [?ast]
  (cl/all 
    (ast :FunctionDeclaration ?ast)))
  
(defn 
  ast|formalparameter
  [?ast]
  (cl/all 
    (ast :FormalParameter ?ast)))

(defn 
  ast|fieldsdeclaration
  [?ast]
  (cl/all 
    (ast :FieldsDeclaration ?ast)))


(def 
  expression-subclasses
  [:Assignment :BackTickExpression :CastExpression :ChainingInstanceCall :CloneExpression 
    :ConditionalExpression :FullyQualifiedTraitMethodReference :NamespaceName :Identifier 
    :IgnoreError :Include :InfixExpression :InstanceOfExpression :LambdaFunctionDeclaration
    :ParenthesisExpression :PHPArrayDereferenceList :PostfixExpression :PrefixExpression
    :Quote :Reference :Scalar :TraitAlias :TraitPrecedence :UnaryOperation :VariableBase
    :ArrayCreation :ClassInstanceCreation :Dispatch :FieldAccess :MethodInvocation
    :FunctionInvocation :ListVariable :StaticDispatch :StaticConstantAccess :StaticFieldAccess :StaticMethodInvocation
    :Variable :ArrayAccess :ReflectionVariable :YieldExpression
   ])

(defn 
  ast|expression
  [?keyw ?ast]
  (cl/conda
    [(el/v+ ?ast) 
     (el/succeeds (instance? Expression ?ast))
     (ast ?keyw ?ast)]
    [(el/v- ?ast)
     (el/contains expression-subclasses ?keyw)
     (ast ?keyw ?ast)]))


(defn 
  ast|classinstancecreation
  [?ast]
  (cl/all 
    (ast :ClassInstanceCreation ?ast)))


(defn
  ast|program
  [?ast]
  (cl/all 
    (ast :Program ?ast)))


(defn 
  ast|classDeclaration 
  [?ast]
  (cl/all
    (ast :ClassDeclaration ?ast)))
