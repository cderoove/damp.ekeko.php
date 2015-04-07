package damp.ekeko.php.plugin;

import org.eclipse.php.internal.core.ast.nodes.ASTNode;
import org.eclipse.php.internal.core.ast.visitor.AbstractVisitor;

import com.google.common.collect.Multimap;

@SuppressWarnings("restriction")
public class PHPTableAddingVisitor extends AbstractVisitor {
	
	Multimap<java.lang.Class<? extends ASTNode>, ASTNode> nodesPerType;
	
	public PHPTableAddingVisitor(Multimap<java.lang.Class<? extends ASTNode>, ASTNode> nodesPerType) {
		this.nodesPerType = nodesPerType;
	}
	
	@Override
	public void preVisit(ASTNode node) {
		super.preVisit(node);
		Class<? extends ASTNode> nodeType = node.getClass();
		nodesPerType.put(nodeType, node);
	}

}
