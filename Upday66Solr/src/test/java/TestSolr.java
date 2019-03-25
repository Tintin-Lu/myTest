import com.itheima.solr.pojo.Product;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.junit.Test;

public class TestSolr {
    /** 定义SolrServer，用来操作Solr */
    private SolrServer solrServer = new HttpSolrServer("http://localhost:8080/solr/collection1");

    @Test
    public void saveOrUpdate() throws Exception{
        Product product = new Product();
        product.setPid("8000");
        product.setName("iphoneX");
        product.setCatalogName("手机");
        product.setPrice(8000d);
        product.setDescription("库克用了都说好");
        product.setPicture("1.jpg");
        solrServer.addBean(product);
        solrServer.commit();
    }
}
