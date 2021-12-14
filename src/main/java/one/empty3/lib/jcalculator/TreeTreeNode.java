package one.empty3.lib.jcalculator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class TreeTreeNode extends TreeNode {
    private final AlgebricTree tree;
    private Method method = null;

    public TreeTreeNode(TreeNode t, Object[] objects, TreeNodeType type) {
        super(t, objects, type);
        tree = new AlgebricTree((String)objects[0], (Map<String, Double>)objects[1]);
        try {
            tree.construct();
            if(objects[2] instanceof String && ((String)objects[2]).length()>0) {
                String call =(String) objects[2];
                if(call.length()>1)
                    method = Math.class.getMethod(call, double.class);
            }
        } catch (AlgebraicFormulaSyntaxException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Double eval() throws TreeNodeEvalException, AlgebraicFormulaSyntaxException {
        double r = 0.0;
        r = tree.eval();
        if(method!=null) {
            try {
                r = (Double) method.invoke(Math.class, r);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return r;

    }
}
