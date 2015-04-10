(ns 
  damp.ekeko.php
  (:refer-clojure :exclude [== type])
  (:use clojure.core.logic)
  (:use [damp.ekeko logic])
  (:use [damp.ekeko])
  (:use [damp.ekeko.php 
         ast ;org.eclipse.php.internal.core.ast.nodes.ASTNode hierarchy
         aststructure ;link between org.eclipse.php.internal.core.ast.nodes.ASTNode hierarchy and org.geclipse.dltk.core.IType hierarchy
         astbindings ;link between org.eclipse.php.internal.core.ast.nodes.ASTNode hierarchy and org.eclipse.php.internal.core.ast.nodes.IBinding hierarchy
         ]))


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
  
  ;;all expressions (belonging to org.eclipse.php.internal.core.ast.nodes.ASTNode hierarchy) and their inferred type (belonging to org.geclipse.dltk.core.IType hierarchy)
  (ekeko* [?keyw ?exp  ?type]
           (ast|expression-type ?keyw ?exp ?type))
  
  ;;all typedeclarations and the type they declare
  (ekeko* [?typedec ?type]
          (ast|typedeclaration-type ?typedec ?type))
  
  ;;inferred type of each expression, together with the declaration of this type
  (ekeko* [?expkey ?exp ?exptype ?typedec] 
          (ast|expression-type ?expkey ?exp ?exptype)
          (ast|typedeclaration-type ?typedec ?exptype))
  
 	;function invocations with each of their parameters
  (ekeko* [?fi ?p]
          (fresh [?ps]
                 (ast :FunctionInvocation ?fi)
                 (has :parameters ?fi ?ps)
                 (contains ?ps ?p)))
    
  ;function invocations and the function binding they resolve to (beloning org.eclipse.php.internal.core.ast.nodes.IBinding hierarchy)
  (ekeko* [?fi ?fb]
            (ast|functioninvocation-binding ?fi ?fb))
  
  
  
  )


