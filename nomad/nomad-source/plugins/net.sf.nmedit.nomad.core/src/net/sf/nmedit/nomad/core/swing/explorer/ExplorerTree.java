/* Copyright (C) 2006 Christian Schneider
 * 
 * This file is part of Nomad.
 * 
 * Nomad is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Nomad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Nomad; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

/*
 * Created on Oct 29, 2006
 */
package net.sf.nmedit.nomad.core.swing.explorer;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import net.sf.nmedit.nmutils.Platform;

public class ExplorerTree extends JTree
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 5896673886593869850L;

    public final static int ACTION_OPEN = 1;
    
    private RootNode root = new RootNode();
    
    public ExplorerTree()
    {
    	DefaultTreeModel model = new DefaultTreeModel(root, true);
    	model.setAsksAllowsChildren(false);
    	TreeSelectionModel selectModel = new DefaultTreeSelectionModel();
    	selectModel.setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
    	setSelectionModel(selectModel);
        setModel(model);
        setUI(new ExplorerTreeUI());
        setFocusable(true);
        
        ToolTipManager.sharedInstance().registerComponent(this);
    }
    
    public void addRootNode(TreeNode node)
    {
        getRoot().add(node);
        fireRootChanged();
    }
    
    public RootNode getRoot()
    {
        return root;
    }
    
    public DefaultTreeModel getModel()
    {
        return (DefaultTreeModel) super.getModel();
    }
    
   /* 
    public ExplorerTreeModel getModel()
    {
        return (ExplorerTreeModel) super.getModel();
    }
 */
    public void updateUI() 
    {
        // ignore - we use custom TreeUI
    }
    
    public void fireNodeStructureChanged(TreeNode node)
    {
    	if (node instanceof FileNode) {
    		Enumeration<TreePath> paths = getExpandedDescendants(new TreePath(((FileNode)node).getPath()));
        	getModel().nodeStructureChanged(node);

    		while (paths.hasMoreElements()) {
    			expandPath(paths.nextElement());
    		}
    	} else {
    		getModel().nodeStructureChanged(node);
    	}
    }
    
    public void fireNodeChanged(TreeNode node)
    {
        getModel().nodeChanged(node);
    }
    
    public void fireNodeStructureChanged(TreePath path)
    {
        fireNodeStructureChanged((TreeNode) path.getLastPathComponent());
    }
    
    public void fireNodeChanged(TreePath path)
    {
        fireNodeChanged((TreeNode) path.getLastPathComponent());
    }

    public void fireRootChanged()
    {
        fireNodeStructureChanged(getRoot());
    }

    public Action createExpandAllAction()
    {
        return new ExpandAllAction(this);
    }

    public Action createCollapseAllAction()
    {
        return new CollapseAllAction(this);
    }
    
    private static ImageIcon getImage(String name)
    {
        return new ImageIcon(ExplorerTree.class.getResource(name));
    }
    
    private static class ExpandAllAction extends AbstractAction implements Runnable
    {
        /**
         * 
         */
        private static final long serialVersionUID = 2170886267799299924L;
        private ExplorerTree tree;

        public ExpandAllAction(ExplorerTree tree)
        {
            this.tree = tree;
            putValue(SHORT_DESCRIPTION, "expand all");
            putValue(SMALL_ICON, getImage("/swing/browser/expandall.gif"));
        }

        public void actionPerformed(ActionEvent e)
        {
            setEnabled(false);
            SwingUtilities.invokeLater(this);
        }

        public void run()
        {
            try
            {
                tree.expandAll();
            }
            finally
            {
                setEnabled(true);
            }
        }
    }
    
    
    private static class CollapseAllAction extends AbstractAction implements Runnable 
    {
        /**
         * 
         */
        private static final long serialVersionUID = -202201397605095160L;
        private ExplorerTree tree;

        public CollapseAllAction(ExplorerTree tree)
        {
            this.tree = tree;
            putValue(SHORT_DESCRIPTION, "collapse all");
            putValue(SMALL_ICON, getImage("/swing/browser/collapseall.gif"));
        }

        public void actionPerformed(ActionEvent e)
        {
            setEnabled(false);
            SwingUtilities.invokeLater(this);
        }

        public void run()
        {
            try
            {
                tree.collapseAll();
            }
            finally
            {
                setEnabled(true);
            }
        }
    }


    public void expandAll()
    {
        int i = 0;
        while( i < getRowCount() ) expandRow( i++ );
    }
    
    public void collapseAll()
    {
        int i = getRowCount()-1;
        while( i >= 0 ) 
        {
            collapseRow( i-- );
        }
    }

    public boolean isPopupTrigger(MouseEvent e, TreeNode treeNode, 
            boolean exactNodeLocation)
    {
        if (Platform.isPopupTrigger(e))
        {            
            TreePath path = getPathForLocation(e.getX(), e.getY());
            if (path == null)
                return false;
            
            if (path.getLastPathComponent() == treeNode)
                return true;
            
            if (exactNodeLocation)
                return false;
            
            path = path.getParentPath();
            while (path != null)
            {
                if (path.getLastPathComponent() == treeNode)
                    return true;
                path = path.getParentPath();
            }
        }
        return false;
    }
    
    
  /*
    public void addAsRoot(Entry entry)
    {
        addRootEntry(entry);
    }
    
    public void addRootEntry(Entry entry)
    {
        globalRoot.add(entry);
        ((DefaultTreeModel)getModel())
        .nodeStructureChanged(globalRoot);
    }


    public void updateRootNode( Treec entry )
    {
        ((DefaultTreeModel)getModel())
        .nodeStructureChanged(entry);
    }
    
    public void updatePath(TreePath path)
    {
        ((DefaultTreeModel)getModel())
        .nodeStructureChanged((TreeNode) path.getLastPathComponent());
    }
    */
    /*
    public void updateNode(TreePath path)
    {
        ((DefaultTreeModel)getModel())
        .nodeStructureChanged((TreeNode)path.getLastPathComponent());

        repaint();
    }*/
    
    /*
    public Entry getSelectedEntry()
    {
        TreePath path = getSelectionPath();
        if (path != null)
        {
            Entry en = (Entry) path.getLastPathComponent();
            if (en!=null)
            {
                return en.getUserObject();
            }
        }
        return null;
    }*/
    
    
}
