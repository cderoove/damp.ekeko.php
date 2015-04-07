package damp.ekeko.php.plugin;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IScriptFolder;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.php.internal.core.PHPVersion;
import org.eclipse.php.internal.core.ast.nodes.ASTNode;
import org.eclipse.php.internal.core.ast.nodes.ASTParser;
import org.eclipse.php.internal.core.ast.nodes.Program;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import damp.ekeko.EkekoPlugin;
import damp.ekeko.ProjectModel;

@SuppressWarnings("restriction")
public class PHPProjectModel extends ProjectModel {

	Multimap<java.lang.Class<? extends ASTNode>, ASTNode> type2nodes;
	
	IScriptProject phpProject;

	private ConcurrentHashMap<ISourceModule,Program> ism2ast;

	public PHPProjectModel(IProject p) {
		super(p);
		System.out.println("Creating PHP project model for: " + p.getName());
		phpProject = DLTKCore.create(p);
		clean();
	}
	
	@Override
	public void clean() {
		super.clean();
		ism2ast = new ConcurrentHashMap<>();
		type2nodes = HashMultimap.create();
	}

	@Override
	public void populate(IProgressMonitor monitor) throws CoreException {
		for(IScriptFolder scriptFolder : phpProject.getScriptFolders()) {
			for (ISourceModule sourceModule : scriptFolder.getSourceModules()) {
				Program program = parseSourceModule(sourceModule);
				gatherNodes(program);
			}
		}
	}

	private Program parseSourceModule(ISourceModule sourceModule) {
		if(sourceModule.isBinary()) 
			return null;
		if(sourceModule.isBuiltin())
			return null;		
		ASTParser parser = ASTParser.newParser(sourceModule);
		try {
			Program ast = parser.createAST(new NullProgressMonitor());
			ism2ast.put(sourceModule, ast);
			return ast;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Iterable<ISourceModule> getISourceModules() {
		return ism2ast.keySet();
	}

	public Iterable<Program> getPrograms() {
		return ism2ast.values();
	}
	
	public Program getProgramForSourceModule(ISourceModule sourceModule) {
		return ism2ast.get(sourceModule);
	}
	
	private void gatherNodes(Program rootAST) {
		PHPTableAddingVisitor addingVisitor = new PHPTableAddingVisitor(type2nodes);
		rootAST.accept(addingVisitor);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends ASTNode> Iterable<T> getNodes(java.lang.Class<T> nodeType) {
		return (Iterable<T>) type2nodes.get(nodeType);
	}
	
	public Iterable<ASTNode> getNodes() {
		return type2nodes.values();
	}

	@Override
	public void processDelta(IResourceDelta delta, IProgressMonitor monitor) throws CoreException {
		super.processDelta(delta,monitor);	
		delta.accept(new EkekoPHPProjectDeltaVisitor());	
	}

	public void processNewSourceModule(ISourceModule ism) {
		Program program = parseSourceModule(ism);
		if(program == null)
			return;
		ism2ast.put(ism, program);
		gatherNodes(program);
	}

	public void processRemovedSourceModule(ISourceModule ism) {
		Program old = ism2ast.remove(ism);
	}

	public void processChangedSourceModule(ISourceModule ism) {
		Program old = ism2ast.remove(ism);
		processNewSourceModule(ism);
	}


	public class EkekoPHPProjectDeltaVisitor implements IResourceDeltaVisitor {
		@Override
		public boolean visit(IResourceDelta delta) throws CoreException {
			IResource resource = delta.getResource();
			IModelElement element = DLTKCore.create(resource);			
			if(element == null) 
				return false; 
			if(element.getElementType() != IModelElement.SOURCE_MODULE) {
				return true; 
			} else {
				ISourceModule ism = (ISourceModule) element;
				switch (delta.getKind()) {
				case IResourceDelta.ADDED:
					EkekoPlugin.getConsoleStream().println("Processing PHP Project Delta: Added ISourceModule");
					processNewSourceModule(ism);
					break;
				case IResourceDelta.REMOVED:
					EkekoPlugin.getConsoleStream().println("Processing PHP Project Delta: Removed ISourceModule");
					processRemovedSourceModule(ism);
					break;
				case IResourceDelta.CHANGED:
					EkekoPlugin.getConsoleStream().println("Processing PHP Project Delta: Changed ISourceModule");
					processChangedSourceModule(ism);
					break;
				}
				return false;
			}
		}
	}




}




