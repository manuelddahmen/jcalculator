/*
 *  This file is part of Empty3.
 *
 *     Empty3 is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Empty3 is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Empty3.  If not, see <https://www.gnu.org/licenses/>. 2
 */

/*
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>
 */

package one.empty3.lib.jcalculator;

import java.util.ArrayList;

/*__
 * Created by Manuel Dahmen on 15-12-16.
 */
public class TreeNode {
    protected Object[] objects;
    protected TreeNodeType type = null;
    private TreeNodeValue value;
    private ArrayList<TreeNode> children = new ArrayList<TreeNode>();
    private TreeNode parent;
    private String expressionString;

    public TreeNode(String expStr) {
        this.parent = null;
        if ("".equals(expStr.trim()))
            expressionString = "0.0";
        this.expressionString = expStr;
    }

    /*
        public TreeNode(TreeNode parent, String expressionString) {
            this.parent = parent;
            this.expressionString = expressionString;
        }
    */
    public TreeNode(TreeNode src, Object[] objects, TreeNodeType clazz) {
        this.parent = src;
        this.objects = objects;
        clazz.instantiate(objects);
        this.type = clazz;
        expressionString = (String) objects[0];
    }

    public Object getValue() {
        return value;
    }

    public void setValue(TreeNodeValue value) {
        this.value = value;
    }


    public Double eval() throws TreeNodeEvalException, AlgebraicFormulaSyntaxException {
        /*if(this instanceof TreeNode) {
            return Double.parseDouble(((TreeNode) this).expressionString);
        }*/

        TreeNodeType cType = (getChildren().size() == 0) ? type : getChildren().get(0).type;

        if (cType instanceof IdentTreeNodeType) {
            System.out.println("cType Ident=" +getChildren().size());
            System.out.println("cType Ident=" +getChildren().get(0).eval());
            return getChildren().get(0).eval();
        } else if (cType instanceof EquationTreeNodeType) {
            return (Double) getChildren().get(0).eval() - (Double) getChildren().get(1).eval() - 0;
        } else if (cType instanceof DoubleTreeNodeType) {
            return cType.eval();
        } else if (cType instanceof VariableTreeNodeType) {
            return cType.eval();//cType.eval();
        } else if (cType instanceof ExponentTreeNodeType) {
            return Math.pow((Double) getChildren().get(0).eval(), (Double) getChildren().get(1).eval());
        } else if (cType instanceof FactorTreeNodeType) {
            if (getChildren().size() == 1) {
                return ((Double) getChildren().get(0).eval()) * getChildren().get(0).type.getSign1();
            }
            double dot = 1;
            for (int i = 0; i < getChildren().size(); i++) {
                TreeNode treeNode = getChildren().get(i);
                double op1;

                if(treeNode.type instanceof FactorTreeNodeType) {
                    op1 = ((FactorTreeNodeType) treeNode.type).getSignFactor();
                    if (op1 == 1)


                        dot *= ((Double) treeNode.eval());
                    else

                        dot /= ((Double) (Double) treeNode.eval());///treeNode.type.getSign1()) *
                }
            }
            return dot;


        } else if (cType instanceof TermTreeNodeType) {
            if (getChildren().size() == 1) {
                return ((Double) getChildren().get(0).eval()) * getChildren().get(0).type.getSign1();
            }
            double sum = 0.0;
            for (int i = 0; i < getChildren().size(); i++) {
                TreeNode treeNode = getChildren().get(i);
                double op1 = treeNode.type.getSign1();
                sum += op1 * (Double) treeNode.eval();
            }


            return sum;
        } else if (cType instanceof TreeTreeNodeType) {
            return ((TreeTreeNode)getChildren().get(0)).eval();
        } else if (cType instanceof SignTreeNodeType) {
            double s1 = ((SignTreeNodeType) cType).getSign();
            if (getChildren().size() > 0)
                return s1 * (Double) getChildren().get(0).eval();
            else
                throw new AlgebraicFormulaSyntaxException("Wrong tree: sign, with no value after");
        }

        return type.eval();

    }

    public ArrayList<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<TreeNode> children) {
        this.children = children;
    }

    public TreeNode getParent() {
        return parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public String getExpressionString() {
        return expressionString;
    }

    public void setExpressionString(String expressionString) {
        this.expressionString = expressionString;
    }


    public String toString() {
        String s = "TreeNode " + this.getClass().getSimpleName() +
                "\nExpression string: " + expressionString +
                (type == null ? "\nType null" :
                        "\nType: " + type.getClass() + "\n " + type.toString()) +
                "\nChildren: "+getChildren().size()+"\n";
        int i = 0;
        for (TreeNode t :
                getChildren()) {
            s += "Child (" + i++ + ") : " + t.toString();
        }
        return s;
    }
}
