import com.itheima.lucene.dao.BookDaoImpl;
import com.itheima.lucene.dao.IBookDao;
import com.itheima.lucene.pojo.Book;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestIndex {

    @Test
    public void createIndex()throws Exception{
        // 采集数据
        IBookDao bookDao = new BookDaoImpl();
        List<Book> bookList = bookDao.findAll();

        // 创建文档集合对象
        List<Document> documents = new ArrayList<>();
        for (Book book : bookList) {
            /**
             * 给文档对象添加域
             * 方法：add（）
             * 参数：TextField
             * TextField参数：
             *   参数一：域的名称
             *   参数二：域的值
             *   参数三：指定是否把域值存储到文档对象中
             */
            Document doc = new Document();
            doc.add(new StringField("id",book.getId() + "", Field.Store.YES));
            doc.add(new TextField("bookName",book.getBookName(),Field.Store.YES));
            doc.add(new DoubleField("bookPrice",book.getPrice(),Field.Store.YES));
            doc.add(new StoredField("bookPic",book.getPic()));
            doc.add(new TextField("bookDesc",book.getBookDesc(),Field.Store.NO));
            documents.add(doc);
        }
        //创建分词器，用于分词
        Analyzer analyzer = new IKAnalyzer();

        // 创建索引库配置对象，用于配置索引库
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_10_3,analyzer);

        // 设置索引库打开模式(每次都重新创建)
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        // 创建索引库目录对象，用于指定索引库存储位置
        Directory directory = FSDirectory.open(new File("E:\\Zprogram\\Upday65Lucene\\Index"));

        // 创建索引库操作对象，用于把文档写入索引库

        IndexWriter indexWriter = new IndexWriter(directory,indexWriterConfig);
        // 循环文档，写入索引库
        for (Document document : documents) {
        /** addDocument方法：把文档对象写入索引库 */
            indexWriter.addDocument(document);
            //提交事务
            indexWriter.commit();
        }
        //释放资源
        indexWriter.close();
    }

    @Test
    public void searchIndex() throws Exception{
        // 创建分析器对象，用于分词
        Analyzer analyzer = new StandardAnalyzer();
        // 创建查询解析器对象
        QueryParser queryParser = new QueryParser("",analyzer);
        // 解释查询字符串，得到查询对象
        Query query = queryParser.parse("bookName:java");
        // 创建索引库存储目录
        Directory directory = FSDirectory.open(new File("E:\\Zprogram\\Upday65Lucene\\Index"));
        // 创建IndexReader读取索引库对象
        IndexReader indexReader = DirectoryReader.open(directory);
        // 创建IndexSearcher，执行搜索索引库
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        /**
         * search方法：执行搜索
         * 参数一：查询对象
         * 参数二：指定搜索结果排序后的前n个（前10个）
         */
        TopDocs topDocs = indexSearcher.search(query, 10);
        // 处理结果集
        System.out.println("总的读取数" + topDocs.totalHits);
        // 获取搜索到得文档数组
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        // ScoreDoc对象：只有文档id和分值信息
        for (ScoreDoc scoreDoc : scoreDocs) {
            System.out.println("------------------");
            System.out.println("文档id: " + scoreDoc.doc
                    + "\t文档分值：" + scoreDoc.score);
            System.out.println("------------------");
            // 根据文档id获取指定的文档
            Document doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println("图书Id：" + doc.get("id"));
            System.out.println("图书名称：" + doc.get("bookName"));
            System.out.println("图书价格：" + doc.get("bookPrice"));
            System.out.println("图书图片：" + doc.get("bookPic"));
            System.out.println("图书描述：" + doc.get("bookDesc"));
            System.out.println("------------------");
        }
        indexReader.close();
    }

    @Test
    public void deleteIndexByTerm() throws Exception{
        // 创建分析器，用于分词
        Analyzer analyzer = new IKAnalyzer();

        // 创建索引库配置信息对象
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_10_3,analyzer);

        // 创建索引库存储目录
        Directory directory = FSDirectory.open(new File("E:\\Zprogram\\Upday65Lucene\\Index"));

        // 创建IndexWriter，操作索引库
        IndexWriter indexWriter = new IndexWriter(directory,indexWriterConfig);

        // 创建条件对象
        Term term = new Term("bookName","java");

        // 使用indexWriter对象，执行删除
        indexWriter.deleteDocuments(term);
        //indexWriter.deleteAll();

        indexWriter.close();
    }

    @Test
    public void updateIndex() throws Exception{
        // 创建分析器，用于分词
        Analyzer analyzer = new IKAnalyzer();

        // 创建索引库配置信息对象
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_10_3,analyzer);

        // 创建索引库存储目录
        Directory directory = FSDirectory.open(new File("E:\\Zprogram\\Upday65Lucene\\Index"));

        // 创建IndexWriter，操作索引库
        IndexWriter indexWriter = new IndexWriter(directory,indexWriterConfig);

        // 创建文档对象
        Document document = new Document();

        // 文档添加域
        document.add(new StringField("id","4399",Field.Store.YES));
        document.add(new TextField("name", "lucene solr dubbo zookeeper", Field.Store.YES));

        // 创建查询条件对象
        Term term = new Term("name","lucene");

        // 使用indexWriter对象，执行更新
        indexWriter.updateDocument(term,document);
        indexWriter.commit();

        indexWriter.close();
    }

    public void search(Query query) throws Exception{
        // 查询语法
        System.out.println("查询语法" + query);

        // 创建索引库存储目录
        Directory directory = FSDirectory.open(new File("E:\\Zprogram\\Upday65Lucene\\Index"));

        // 创建IndexReader读取索引库对象
        IndexReader indexReader = DirectoryReader.open(directory);

        // 创建IndexSearcher，执行搜索索引库
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        /**
         * search方法：执行搜索
         * 参数一：查询对象
         * 参数二：指定搜索结果排序后的前n个（前10个）
         */
        TopDocs topDocs = indexSearcher.search(query,10);

        // 处理结果集
        System.out.println("命中记录" + topDocs.totalHits);

        // 获取搜索到得文档数组
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        // ScoreDoc对象：只有文档id和分值信息
        for (ScoreDoc scoreDoc : scoreDocs) {
            System.out.println("------------------");
            System.out.println("获取文档ID：" + scoreDoc.doc + "\t获取分值:" + scoreDoc.score);
            System.out.println("------------------");
            // 根据文档id获取指定的文档
            Document document = indexSearcher.doc(scoreDoc.doc);
            System.out.println("图书Id：" + document.get("id"));
            System.out.println("图书名称：" + document.get("bookName"));
            System.out.println("图书价格：" + document.get("bookPrice"));
            System.out.println("图书图片：" + document.get("bookPic"));
            System.out.println("图书描述：" + document.get("bookDesc"));
        }

        // 释放资源
        indexReader.close();
    }

    @Test
    public void testSearchTermQuery() throws Exception {
        //TermQuery	不使用分析器，对关键词做精确匹配搜索。比如：订单编号、身份证号
        TermQuery termQuery = new TermQuery(new Term("bookName","java"));
        search(termQuery);
    }

    @Test
    public void testSearchNumericRangQuery() throws Exception{
        // 创建查询对象
        /**
         * 参数说明
         *  field：域的名称
         *  min：最小范围边界值
         *  max：最大范围边界值
         *  minInclusive:是否包含最小边界值
         *  maxInclusive:是否包含最大边界值
         */
        Query query = NumericRangeQuery.newDoubleRange("bookPrice",80d ,100d ,false ,true);
        search(query);
    }

    @Test
    public void testBooleanQuery() throws Exception{
        // 创建查询对象一
        TermQuery q1 = new TermQuery(new Term("bookName","java"));
        // 创建查询对象二
        Query q2 = NumericRangeQuery.newDoubleRange("bookPrice",80d ,100d ,true,true);

        // 创建组合查询条件对象
        BooleanQuery q = new BooleanQuery();

        /*
        1.	MUST与MUST表示“与”，即“交集”
        2.	MUST与MUST NOT，包含前者，排除后者
        3.	MUST NOT与MUST NOT没有意义
        4.	SHOULD与MUST表示MUST，SHOULD失去意义
        5.	SHOULD与SHOULD表示“或”
        */
        q.add(q1,BooleanClause.Occur.MUST);
        q.add(q2,BooleanClause.Occur.MUST);
        search(q);
    }
}
