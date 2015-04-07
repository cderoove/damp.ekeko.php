(ns 
  damp.ekeko.php
  (:refer-clojure :exclude [== type])
  (:use clojure.core.logic)
  (:use [damp.ekeko logic])
  (:use [damp.ekeko])
  (:use [damp.ekeko.php ast aststructure]))


(defn
  make-query-views-php-aware!
  []
  (intern 'damp.ekeko.gui 'label-provider-class damp.ekeko.php.plugin.PHPLabelProvider))

(make-query-views-php-aware!)

(comment
  ;;example repl session
  (use 'damp.ekeko.php)
  (in-ns 'damp.ekeko.php)
  
  ;;count of all ast nodes 
  (count (ekeko [?keyw ?ast] (ast ?keyw ?ast)))
      
  ;;all nodes of type FunctionInvocation
  (ekeko* [?ast] (ast :FunctionInvocation ?ast))

  (ekeko* [?ast ?prop ?propvalue]
          (ast :FunctionInvocation ?ast)
          (has ?prop ?ast ?propvalue))
  
  ;;all expressions
  (ekeko* [?keyw ?ast] 
          (ast|expression ?keyw ?ast))
  
  ;;all expressions and their inferred type
  (ekeko* [?keyw ?exp  ?type]
           (ast|expression-type ?keyw ?exp ?type))
  
  ;;all typedeclarations and the type they declare
  (ekeko* [?typedec ?type]
          (ast|typedeclaration-type ?typedec ?type))
  
  ;;inferred type of each expression, together with the declaration of this type
  (ekeko* [?expkey ?exp ?exptype ?typedec] 
          (ast|expression-type ?expkey ?exp ?exptype)
          (ast|typedeclaration-type ?typedec ?exptype))
  
  
  
  )


