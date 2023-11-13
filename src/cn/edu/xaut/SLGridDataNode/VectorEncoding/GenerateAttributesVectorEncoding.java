    /*
     * To change this template, choose Tools | Templates
     * and open the template in the editor.
     */

    package cn.edu.xaut.SLGridDataNode.VectorEncoding;

/**
 * @author greensubmarine
 */

    import cn.edu.xaut.SLGridDataNode.VectorEncoding.Util.AttributePreVectorDietzEncodingBean;
    import cn.edu.xaut.SLGridDataNode.VectorEncoding.Util.AttributeVectorDietzEncodingBean;
    import org.jdom.Attribute;
    import org.jdom.Document;
    import org.jdom.Element;
    import org.jdom.input.SAXBuilder;

    import java.util.*;

    public class GenerateAttributesVectorEncoding {

    private ArrayList<AttributePreVectorDietzEncodingBean> attributelists = new ArrayList<AttributePreVectorDietzEncodingBean>();
    private ArrayList<AttributeVectorDietzEncodingBean> attributevectordietzencodebean = new ArrayList<AttributeVectorDietzEncodingBean>();
    GenerateElementsVectorEncoding elementvectorencoding = new GenerateElementsVectorEncoding();
    private Map<String, String> TempElementDietzOrder = new HashMap<String, String>();
    private Map<Integer, int[]> Vectormaps = new HashMap<Integer, int[]>();
    private Map<int[], String> AttributeNameandVectorDietzPreOrderMap = new HashMap<int[], String>();
    private Map<int[], String> AttributeNameandVectorDietzPostOrderMap = new HashMap<int[], String>();
    VectorEncodingFunctions vectorfunctions = new VectorEncodingFunctions();
    private int AttributePreOrder = 0;
    private int elementcount = 0;

    public int GetElementCount(Element root) {

        List elementlist = new ArrayList();
        elementlist = root.getContent();
        Iterator iter = elementlist.iterator();
        elementcount++;
        while (iter.hasNext()) {
            Object obj = iter.next();
            if (obj instanceof Element) {
                Element child = (Element) obj;
                elementcount = GetElementCount(child);
            }
        }
        return elementcount;
    }

    public void GetAttributeName(int DocumentID, Element root) {
        List elementlist = new ArrayList();
        elementlist = root.getContent();
        AttributePreOrder++;
        String formattedattributepreorder = Integer.toString(AttributePreOrder);
        List<Attribute> attributelist = new ArrayList<Attribute>();
        attributelist = root.getAttributes();
        for (int i = 0; i < attributelist.size(); i++) {
            Attribute attributenode = attributelist.get(i);
            String attributename = attributenode.getName();
            String namespaceprefix = attributenode.getNamespacePrefix();
            String attributevalue = attributenode.getValue();
            attributelists.add(new AttributePreVectorDietzEncodingBean(DocumentID, attributename, attributevalue, formattedattributepreorder, namespaceprefix));
        }
        Iterator iter = elementlist.iterator();
        while (iter.hasNext()) {
            Object obj = iter.next();
            if (obj instanceof Element) {
                Element child = (Element) obj;
                GetAttributeName(DocumentID, child);
            }
        }
    }

    public ArrayList<AttributeVectorDietzEncodingBean> getAttributeBean(int DocID, String FileName) {
        this.GenerateAttributesPreandPostVectorEncoding(DocID, FileName);
        return attributevectordietzencodebean;
    }

    public void GenerateAttributesPreandPostVectorEncoding(int DocID, String FileName) {
        String DietzXMLFile = elementvectorencoding.ProductNewXMLContainsDietzPreOrder(FileName);
        try {
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(FileName);
            Element root = doc.getRootElement();
            elementcount = this.GetElementCount(root);
            System.out.println(elementcount);
            this.GetAttributeName(DocID, root);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        TempElementDietzOrder = elementvectorencoding.GetDietzOrderMap(DietzXMLFile);
        Vectormaps = VectorEncodingFunctions.GetDietzVectorMap(elementcount);
        if (attributelists == null) {
            System.out.println("data transfer failer");

        } else {
            Iterator attributelistiter = attributelists.iterator();
            while (attributelistiter.hasNext()) {
                AttributePreVectorDietzEncodingBean attributeprebean = (AttributePreVectorDietzEncodingBean) attributelistiter.next();
                String PreVectorAttributeEncoding = "[" + Vectormaps.get(Integer.parseInt(attributeprebean.GetAttributeVectorPreDietzOrder()))[0] + "," + Vectormaps.get(Integer.parseInt(attributeprebean.GetAttributeVectorPreDietzOrder()))[1] + "]";
                AttributeNameandVectorDietzPreOrderMap.put(Vectormaps.get(Integer.parseInt(attributeprebean.GetAttributeVectorPreDietzOrder())), attributeprebean.GetAttributeName());
                String TempElementPostDietzOrder = TempElementDietzOrder.get(attributeprebean.GetAttributeVectorPreDietzOrder());
                String PostVectorAttributeEncoding = "[" + Vectormaps.get(Integer.parseInt(TempElementPostDietzOrder))[0] + "," + Vectormaps.get(Integer.parseInt(TempElementPostDietzOrder))[1] + "]";
                AttributeNameandVectorDietzPostOrderMap.put(Vectormaps.get(Integer.parseInt(TempElementPostDietzOrder)), attributeprebean.GetAttributeName());
                attributevectordietzencodebean.add(new AttributeVectorDietzEncodingBean(DocID, attributeprebean.GetAttributeName(), attributeprebean.GetAttributeValue(), PreVectorAttributeEncoding, attributeprebean.GetAttributePrefix(), PostVectorAttributeEncoding));
            }
        }
    }

    public static void main(String args[]) {

        GenerateAttributesVectorEncoding attributeencode = new GenerateAttributesVectorEncoding();
        ArrayList<AttributeVectorDietzEncodingBean> attributevectordietzencodebeans = attributeencode.getAttributeBean(0, "example.xml");
        for (int i = 0; i < attributevectordietzencodebeans.size(); i++) {
            System.out.println("The Attribute name is:" + attributevectordietzencodebeans.get(i).GetAttributeName());
            System.out.println("The Attribute value is:" + attributevectordietzencodebeans.get(i).GetAttributeValue());
            System.out.println("The Attribute pre vectordietz encoding is:" + attributevectordietzencodebeans.get(i).GetAttributeVectorPreDietzOrder());
            System.out.println("The Attribute post vectordietz encoding is:" + attributevectordietzencodebeans.get(i).GetAttributeVectorPostDietzOrder());
            System.out.println("The Attribute Prefix is:" + attributevectordietzencodebeans.get(i).GetAttributePrefix());
        }

    }
    }
