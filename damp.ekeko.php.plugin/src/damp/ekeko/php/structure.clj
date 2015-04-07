(ns damp.ekeko.php.structure
  (:refer-clojure :exclude [== type])
  (:require [clojure.core [logic :as cl]])
  (:require 
    [damp.ekeko [logic :as el]]
    [damp.ekeko.php [phpprojectmodel :as phpprojectmodel]])
  (:import 
    [org.eclipse.dltk.core IModelElement]))

