(ns 
  damp.ekeko.php.astnode
  (:require 
    [damp.util.interop]
    [damp.ekeko.jdt
     [astnode :as jdtastnode]] ;todo: extract commonalities to shared namespace
    [damp.ekeko.php
     [phpprojectmodel :as pm]])
  (:import 
    [damp.ekeko.php.plugin PHPLabelProvider]
    [org.eclipse.php.internal.core.ast.nodes 
     ASTNode StructuralPropertyDescriptor ChildListPropertyDescriptor ChildPropertyDescriptor SimplePropertyDescriptor]))


;; Disable XML representation of ASTNode on REPL to avoid stackoverflows

  
(defn
  node-string|source
  [node]
  (PHPLabelProvider/asString node))

(defmethod 
  clojure.core/print-method 
  ASTNode 
  [node writer]
  (.write writer (node-string|source node)))


;; Auxiliary functions


(defn
  keyword-for-class
  [class]
  (keyword (.getSimpleName ^Class class)))

(defn
  class-for-qualifiedname 
  [name]
  (Class/forName name))

(def
  class-for-packagename-simplename
  (memoize 
    (fn [prefix name]
      (class-for-qualifiedname (str prefix "." name)))))
  
(defn
  class-for-keyword-package
  [key packagename]
  (class-for-packagename-simplename packagename (name key)))


(def
  packagename|pdt
  "org.eclipse.php.internal.core.ast.nodes")


(defn
  class-for-keyword
  [key]
  (class-for-keyword-package key packagename|pdt))

(defn
  keyword-for-class-of
  [node]
  (keyword-for-class (class node)))
 

(defn 
  ast?
  [x]
  (instance? ASTNode x))

(defprotocol 
  IAST
  (reifiers [this] 
    "Returns a map of keywords to reifier functions. The latter will return an Ekeko-specific child of the AST node."))



(def value? jdtastnode/value?)
(def value|list? jdtastnode/lstvalue?)
(def value|null? jdtastnode/nilvalue?)
(def value|primitive? jdtastnode/primitivevalue?)
(def value-unwrapped jdtastnode/value-unwrapped)
(def owner jdtastnode/owner)



(defn
  ast-of-keyword?
  [node key]
  (and 
    (ast? node)
    (instance? (class-for-keyword key) node)))


(defn
  asts-for-keyword
  [key]
  (let [clazz (class-for-keyword key)]
    (mapcat 
      (fn [projectmodel]  (pm/pm-asts|type projectmodel clazz))
      (pm/php-project-models))))

(defn
  asts
  []
  (mapcat 
    (fn [projectmodel] (pm/pm-asts projectmodel))
    (pm/php-project-models)))


;; Property descriptors
;; --------------------

(defn property-descriptor-id [^StructuralPropertyDescriptor p]
  (.getId p))

(defn property-descriptor-simple? [^StructuralPropertyDescriptor p]
  (.isSimpleProperty p))

(defn property-descriptor-list? [^StructuralPropertyDescriptor p]
  (.isChildListProperty p))

(defn property-descriptor-child? [^StructuralPropertyDescriptor p]
  (.isChildProperty p))

(defn ^Class property-descriptor-owner-node-class [^StructuralPropertyDescriptor p]
  (.getNodeClass p))

(defn property-descriptor-child-node-class [^ChildPropertyDescriptor p]
  (.getChildType p))

(defn property-descriptor-element-node-class [^ChildListPropertyDescriptor p]
  (.getElementType p))

(defn property-descriptor-value-class [^SimplePropertyDescriptor p]
  (.getValueType p))

(defn ekeko-keyword-for-property-descriptor [p] 
  (keyword (property-descriptor-id p)))

(defn node-property-value [^ASTNode n ^StructuralPropertyDescriptor p]
  (.getStructuralProperty n p))




(defn 
  nodeclass-property-descriptors 
  [^Class cls]
  (try 
    (damp.util.interop/get-invisible-field cls "PROPERTY_DESCRIPTORS" nil)
    (catch java.lang.NoSuchFieldException e 
       (damp.util.interop/get-invisible-field cls "PROPERTY_DESCRIPTORS_PHP5" nil))))
  

(extend-protocol
  jdtastnode/IHasOwner
  ASTNode 
  (owner [this] (.getParent ^ASTNode this))
  (owner-property [this] (.getLocationInParent ^ASTNode this)))

(extend-protocol
  jdtastnode/IHasProperties
  ASTNode
  (property-value [n p] 
    (node-property-value n p)))


(def 
  node-ekeko-properties-for-class
  (memoize
    (fn [^Class nc]
      (let [descriptors (nodeclass-property-descriptors nc)]
        (zipmap (map (fn [^StructuralPropertyDescriptor p] 
                       (keyword (property-descriptor-id p)))
                     descriptors)
                (map (fn [^StructuralPropertyDescriptor p]
                       (cond
                         (property-descriptor-child? p)
                         (fn [n]
                           (let [value (node-property-value n p)]
                             (if 
                               (ast? value)
                               value
                               (jdtastnode/make-value|nil n p))))
                         (property-descriptor-list? p)
                         (fn [n]
                           (jdtastnode/make-value|lst n p))
                         :default
                         (fn [n]
                          (jdtastnode/make-value|primitive n p))))
                     descriptors)
                )))))

(defn
  node-ekeko-properties
  [node]
  (node-ekeko-properties-for-class (class node)))


(defn 
  node-ekeko-prop2val 
  [node]
  (let [propmap (node-ekeko-properties node)]
    (zipmap (keys propmap)
            (for [[propkeyword propretrieving] propmap]
              (let [val (propretrieving node)]
                (if
                  (ast? val)
                  val
                  (value-unwrapped val)))))))


(defn
  node-ekeko-values
  [node]
  (vals (node-ekeko-prop2val node)))


(defn
  node-ancestors
  [^ASTNode n]
  (loop [ancestors []
         parent (.getParent n)]
    (if 
      parent
      (recur (conj ancestors parent)
             (.getParent parent))
      ancestors)))
 


(defn
  node-ancestors|type 
  [node type]
  (let [classtype (class-for-keyword type)]
    (filter 
      (fn [n]
        (instance? classtype  n))
      (node-ancestors node))))


(defn
  value-ancestors
  [v]
  (loop [ancestors []
         parent (owner v)]
    (if 
      parent
      (recur (conj ancestors parent)
             (owner parent))
      ancestors)))


  (extend 
  ASTNode
  IAST
  {:reifiers (fn [this] 
               (node-ekeko-properties-for-class (class this)))})
                                                  
                            
(defn 
  node-propertyvalues
  [n]
  (map 
    (fn [retrievalf] (retrievalf n))
    (vals (reifiers n))))

(defn
  nodeorvalue-offspring
  [^ASTNode n]
  (loop [offspring []
         worklist [n]]
    (if 
      (empty? worklist)
      offspring
      (let [current 
            (first worklist)
            values  
            (cond (ast? current) (node-propertyvalues current)
                  (value|list? current) (value-unwrapped current)
                  :default [])]
        (recur (into offspring values)
               (into (rest worklist) 
                     (filter (fn [value]
                               (or (ast? value)
                                   (value|list? value)))
                             values)))))))                   
    
      


(defn
  nodeorvalue-offspring|type 
  [node type]
  (let [classtype (class-for-keyword type)]
    (filter 
      (fn [n]
        (instance? classtype  n))
      (nodeorvalue-offspring node))))

(defn 
  fieldaccesses 
  [classes]
  (loop [classes classes
         fieldaccess {}]
    (if 
      (empty? classes)
      fieldaccess
      (let [class (first classes)
            values (nodeorvalue-offspring|type class :FieldAccess)]
        (recur (rest classes)
               (assoc fieldaccess class values))))))

(defn
  node-propkey-value
  [node propkey]
  
  )


(defn 
  class-by-name
  [name]
  (let [classes (asts-for-keyword :ClassDeclaration)]
    (filterv
      (fn [class]
          (= name (:name (node-ekeko-prop2val (:name (node-ekeko-prop2val class))))))
      classes)))




