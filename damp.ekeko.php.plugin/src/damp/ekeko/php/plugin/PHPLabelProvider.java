package damp.ekeko.php.plugin;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.php.internal.core.ast.nodes.ASTNode;
import org.eclipse.php.internal.core.ast.rewrite.ASTRewriteFlattener;
import org.eclipse.swt.graphics.Image;

@SuppressWarnings("restriction")
public class PHPLabelProvider extends LabelProvider {

	public static String asString(ASTNode node) {
		return ASTRewriteFlattener.asString(node, null);
	}

	@Override 
	public Image getImage(Object object) {
		if(object instanceof ASTNode) {
			return null;
		} else {
			return super.getImage(object);
		}
	}

	@Override
	public String getText(Object element) {
		if(element instanceof ASTNode) {
			//PHP ASTNodes are  slow to print, and don't contain actual information
			//return element.getClass().getName();
			return asString((ASTNode) element);
		} else {
			return super.getText(element);
		}	
	}
}