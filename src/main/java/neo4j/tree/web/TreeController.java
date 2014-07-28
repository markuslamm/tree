/**
 * 
 */
package neo4j.tree.web;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import neo4j.tree.domain.Neo4jNode;
import neo4j.tree.service.TreeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import com.google.common.collect.Lists;

/**
 * Web controller for tree operations. Uses Thymeleaf templates as view resolver
 * 
 * @author Markus Lamm
 */
@Controller
@RequestMapping("/")
public class TreeController
{
    private static final Logger LOG = LoggerFactory.getLogger(TreeController.class);

    private TreeService service;

    private Validator validator;

    @ModelAttribute("allNodes")
    public List<Neo4jNode> getAllNodes() {
        List<Neo4jNode> result = service.findAll();
        return result;
    }

    /**
     * Shows index page, lists the tree structure or links to a form to create the root node
     * 
     * @param model view model
     * @return view name
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(final Model model) {
        final Neo4jNode rootNode = service.getRootNode();
        if (null != rootNode) {
            final List<Neo4jNode> ordereredNodes = service.getPreordered(rootNode);
            model.addAttribute("orderedNodes", ordereredNodes);
        }
        return ViewNames.INDEX;
    }

    /**
     * Creates the form for creating the root node
     * 
     * @param model view model
     * @return view name
     */
    @RequestMapping(value = "/node", method = RequestMethod.GET)
    public String createRootNodeForm(final Model model) {
        model.addAttribute("treeNodeForm", new TreeNodeForm());
        return ViewNames.CREATE_TREE;
    }

    /**
     * Root node form submission
     * 
     * @param form the command object
     * @param model vieww model
     * @return redirect to index
     */
    @RequestMapping(value = "/node", method = RequestMethod.POST)
    public String submitRootNodeForm(final TreeNodeForm form, final Model model) {
        final Set<? extends ConstraintViolation<?>> violations = validator.validate(form);
        if (violations.size() > 0) {
            LOG.debug("Validation errors occured");
            model.addAttribute("rootHasErrors", true);
            model.addAttribute("errorList", getErrorMessages(violations));
            return ViewNames.CREATE_TREE;
        }
        Neo4jNode node = service.createTree(form.getName());
        return "redirect:/";
    }

    /**
     * Creates a command object for node with the given nodeUuid
     * 
     * @param model view model
     * @param nodeUuid node identifier
     * @return view name
     */
    @RequestMapping(value = "/node/{nodeUuid}", method = RequestMethod.GET)
    public String editNodeForm(final Model model, final @PathVariable String nodeUuid) {
        final Neo4jNode node = service.findByUuid(nodeUuid);
        model.addAttribute("treeNodeForm", createForm(node));
        List<Neo4jNode> children = service.findByParent(node);
        model.addAttribute("children", children);
        model.addAttribute("parentUuid", node.getUuid());
        final String parentName = (node.getParent() == null) ? "null" : node.getParent().getName();
        model.addAttribute("parentName", parentName);
        model.addAttribute("isRoot", node.isRoot());
        model.addAttribute("childUuid", node.getUuid());
        return ViewNames.EDIT_NODE;
    }

    /**
     * Edit node form submission
     * 
     * @param form the command object
     * @param model view model
     * @param nodeUuid
     * @return redirect to index
     */
    @RequestMapping(value = "/node/{nodeUuid}", method = RequestMethod.POST)
    public String submitEditNodeForm(final TreeNodeForm form, final Model model, final @PathVariable String nodeUuid) {
        final Set<? extends ConstraintViolation<?>> violations = validator.validate(form);
        Neo4jNode node = service.findByUuid(nodeUuid);
        node.setName(form.getName());
        node = service.updateNode(form, nodeUuid);
        return "redirect:/";
    }

    /**
     * Creates a command object for creating a child note for a node with the given nodeUuid
     * 
     * @param model view model
     * @param nodeUuid the parent identifier
     * @return view name
     */
    @RequestMapping(value = "/node/{nodeUuid}/add", method = RequestMethod.GET)
    public String addChildForm(final Model model, final @PathVariable String nodeUuid) {
        final Neo4jNode node = service.findByUuid(nodeUuid);
        model.addAttribute("treeNodeForm", createForm(node));
        List<Neo4jNode> children = service.findByParent(node);
        model.addAttribute("children", children);
        model.addAttribute("addChild", true);
        model.addAttribute("parentUuid", node.getUuid());
        model.addAttribute("childNodeForm", new TreeNodeForm());
        model.addAttribute("isRoot", node.isRoot());
        final String parentName = (node.getParent() == null) ? "null" : node.getParent().getName();
        model.addAttribute("parentName", parentName);
        model.addAttribute("isRoot", node.isRoot());
        return ViewNames.EDIT_NODE;
    }

    /**
     * Add child form submission.
     * 
     * @param childNodeForm command object
     * @param model view model
     * @param parentUuid parent node identifier
     * @return redirect to index
     */
    @RequestMapping(value = "/node/{parentUuid}/add", method = RequestMethod.POST)
    public String submitAddChildNodeForm(final TreeNodeForm childNodeForm, final Model model, final @PathVariable String parentUuid) {
        final Set<? extends ConstraintViolation<?>> violations = validator.validate(childNodeForm);
        if (violations.size() > 0) {
            final Neo4jNode node = service.findByUuid(parentUuid);
            LOG.debug("Validation errors occured");
            model.addAttribute("childHasErrors", true);
            model.addAttribute("errorList", getErrorMessages(violations));
            model.addAttribute("treeNodeForm", createForm(node));
            List<Neo4jNode> children = service.findByParent(node);
            model.addAttribute("children", children);
            model.addAttribute("parentUuid", node.getUuid());
            final String parentName = (node.getParent() == null) ? "null" : node.getParent().getName();
            model.addAttribute("parentName", parentName);
            model.addAttribute("isRoot", node.isRoot());
            model.addAttribute("childUuid", node.getUuid());
            return ViewNames.EDIT_NODE;

        }
        Neo4jNode node = new Neo4jNode(childNodeForm.getName());
        Neo4jNode added = service.addNode(node, parentUuid);
        return "redirect:/";
    }

    /**
     * Handles delete requests for a node with the given nodeUuid
     * 
     * @param model view model
     * @param nodeUuid node identifier
     * @return redirects to index
     */
    @RequestMapping(value = "/node/{nodeUuid}/remove", method = RequestMethod.GET)
    public String deleteChildNode(final Model model, final @PathVariable String nodeUuid) {
        service.deleteNode(nodeUuid);
        return "redirect:/";
    }

    /**
     * Helper method to extract validation messages from ValidationExceptions
     * 
     * @param violations constraint violations
     * @return list with messages
     */
    private static List<String> getErrorMessages(final Set<? extends ConstraintViolation<?>> violations) {
        final List<String> msgs = Lists.newArrayList();
        for (final ConstraintViolation<?> constraintViolation : violations) {
            // msgs.add(constraintViolation.getMessage());
            // TODO
            // this is a hack! validation message interpolating does not work,
            // because just one violation may occur (name), this seems legit here
            msgs.add("You must provide at least a name for a TreeNode");
        }
        return msgs;
    }

    /**
     * Helper method to initialize the command object
     * 
     * @param node the node with required data
     * @return initialized command object
     */
    private static TreeNodeForm createForm(final Neo4jNode node) {
        final TreeNodeForm form = new TreeNodeForm();
        form.setNodeId(node.getNodeId());
        form.setUuid(node.getUuid());
        form.setCreatedDate(node.getCreatedDate().toString());
        form.setLastModifiedDate(node.getLastModifiedDate().toString());
        form.setName(node.getName());
        form.setChildren(node.getChildren());
        form.setParent(node.getParent());
        return form;
    }

    @Inject
    public void setService(final TreeService service) {
        this.service = service;
    }

    @Inject
    public void setValidator(final Validator validator) {
        this.validator = validator;
    }
}
