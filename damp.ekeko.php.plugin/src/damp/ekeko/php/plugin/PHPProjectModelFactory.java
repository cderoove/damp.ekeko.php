package damp.ekeko.php.plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.resources.IProject;
import org.eclipse.php.internal.core.project.PHPNature;

import damp.ekeko.IProjectModel;
import damp.ekeko.IProjectModelFactory;

@SuppressWarnings("restriction")
public class PHPProjectModelFactory implements IProjectModelFactory {

	@Override
	public IProjectModel createModel(IProject project) {
		return new PHPProjectModel(project);
	}

	@Override
	public Collection<String> applicableNatures() {
		Collection<String> result =  new ArrayList<String>(1);
		result.add(PHPNature.ID);
		return result;
	}

	@Override
	public Collection<IProjectModelFactory> conflictingFactories(IProject p, Collection<IProjectModelFactory> applicableFactories) {
		return Collections.emptySet();
	}

}
