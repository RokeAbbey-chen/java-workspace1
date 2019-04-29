package bean;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Crawler {

    ExecutorService pool = new ThreadPoolExecutor(5, 200, 0, TimeUnit.NANOSECONDS, new LinkedBlockingQueue<>());
    private String outputName;
    private File outputDir;
    private AtomicInteger outputIndex = new AtomicInteger(0);
    private static Calendar calendar = Calendar.getInstance();

    public void crawl() throws IOException {
        URL url = new URL("https://image.baidu.com/search/index?tn=baiduimage&ct=201326592&lm=-1&cl=2&ie=gb18030&word=%C3%C0%C5%AE&fr=ala&ala=1&alatpl=adress&pos=0&hs=2&xthttps=111111");
        URLConnection urlConnection = url.openConnection();

        StringBuilder content = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
            String line = null;
            while((line = reader.readLine()) != null){
                content.append(line);
            }
        }

//        Pattern p = Pattern.compile("href(?:\\s*)=(?:\\s*)([\"'])(/search/detail.*?)\\1");
//        http://imgsrc.baidu.com/imgad/pic/item/adaf2edda3cc7cd94435a7fa3301213fb80e91fd.jpg
        Pattern p = Pattern.compile("\"objURL\":\"(http://imgsrc.baidu.com/imgad/pic/item/.*?)\"");
        Matcher m = p.matcher(content.toString());
        ArrayList<URL> picList = new ArrayList<>();
        while(m.find()){
            String urlstr = m.group(1);
            System.out.println(urlstr);
            picList.add(new URL(urlstr));
        }

        File imgdir = new File("./img/");
        if (!imgdir.exists()){ imgdir.mkdir(); }
        for (URL u : picList){
            String fileName = u.getFile();
            fileName = fileName.substring(fileName.lastIndexOf("/"), fileName.length());
            try( InputStream input = u.openStream();
                 FileOutputStream output = new FileOutputStream(imgdir.getPath() + fileName);
            ){
                byte[] bytes = new byte[2048];
                int len = -1;
                while((len = input.read(bytes)) >= 0){
                    output.write(bytes, 0, len);
                }
            }
        }

    }
    /*
    /search/detail?ct=503316480&amp;z=undefined&amp;tn=baiduimagedetail&amp;ipn=d&amp;word=%E7%BE%8E%E5%A5%B3&amp;step_word=&amp;ie=utf-8&amp;in=&amp;cl=2&amp;lm=-1&amp;st=undefined&amp;cs=964687523,1564876761&amp;os=3929004426,1901667219&amp;simid=1219065893,529781331&amp;pn=0&amp;rn=1&amp;di=2716852840&amp;ln=3976&amp;fr=&amp;fmq=1527747833502_R&amp;fm=&amp;ic=undefined&amp;s=undefined&amp;se=&amp;sme=&amp;tab=0&amp;width=undefined&amp;height=undefined&amp;face=undefined&amp;is=0,0&amp;istype=0&amp;ist=&amp;jit=&amp;bdtype=13&amp;spn=0&amp;pi=0&amp;gsm=0&amp;hs=2&amp;objurl=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Fadaf2edda3cc7cd94435a7fa3301213fb80e91fd.jpg&amp;rpstart=0&amp;rpnum=0&amp;adpicid=0
    * */





    public HashSet<URL> crawl(Collection<URL> urls, Filter filter) throws IOException, ExecutionException, InterruptedException {
        HashSet<URL> result = new HashSet<>();
        List<Future<HashSet<URL>>> futures = new LinkedList<>();
        for (URL url : urls){
            futures.add(pool.submit(new DownloadCallable(url, filter)));
        }
        List<Future<HashSet<URL>>> temp = new LinkedList<>();
        while (!futures.isEmpty()){

            for (Future<HashSet<URL>> f : futures){
                if(f.isDone()){ result.addAll(f.get()); }
                else { temp.add(f); }
            }

            if (temp.isEmpty()){ break; }
            List<Future<HashSet<URL>>> t = futures;
            futures = temp;
            temp = t;
            temp.clear();
            Thread.sleep(1000);
        }
        return result;
    }

    public HashSet<URL> crawlStringURL(Collection<String> strUrls, Filter filter) throws ExecutionException, InterruptedException {
        try {
            Collection<URL> urls = new HashSet<>();//strUrls.getClass().newInstance();
            for (String s : strUrls){
                try {
                    urls.add(new URL(s));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            return crawl(urls, filter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private class DownloadCallable implements Callable<HashSet<URL>>{

        private URL url;
        private Filter filter;

        public DownloadCallable(URL url, Filter filter){
            this.url = url;
            this.filter = filter;
        }

        @Override
        public HashSet<URL> call() throws Exception {
            URLConnection conn = url.openConnection();
            try( BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    System.out.println(line);
                }
                String input;
                LinkedList<Future<Long>> futures = new LinkedList<>();
                LinkedList<Future<Long>> temp = new LinkedList<>();
                int i = 0;
                for(URL downloadURL : filter.downloadMatch(input = sb.toString())){
                    futures.add(pool.submit(new DownloadHelper(downloadURL)));
                }
                System.out.println("共有" + futures.size() + "个下载任务");

                while(!futures.isEmpty()){
                    for (Future<Long> f : futures){
                        if (f.isDone()){
                            System.out.println("耗时" + f.get() / 1000 + "s");
                        }
                        else { temp.add(f); }
                    }
                    if (temp.isEmpty()){ break; }
                    LinkedList<Future<Long>> t = futures ;
                    futures = temp;
                    temp = t;
                    temp.clear();
                    Thread.sleep(1000);
                }

                return filter.enterIntoMatch(input);
            }
        }

        private long download(URL url) throws IOException {
            long start = System.currentTimeMillis();
            String suffix = url.getFile().substring(url.getFile().lastIndexOf('.'));
            suffix = suffix.lastIndexOf('?') >= 0 ? suffix.substring(0, suffix.lastIndexOf('?')): suffix;
            try( BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputDir.getCanonicalPath()+"/"+ outputIndex.incrementAndGet()+suffix));
                    BufferedInputStream inputStream = new BufferedInputStream(url.openConnection().getInputStream())){
                byte[] bytes = new byte[1024];
                int len = 0;
                while((len = inputStream.read(bytes)) != -1){
                    outputStream.write(bytes, 0, len);
                }
            }
            return System.currentTimeMillis() - start;
        }

        private class DownloadHelper implements Callable<Long>{

            private URL url;
            public DownloadHelper(URL url){
                this.url = url;
            }

            @Override
            public Long call() throws Exception {
                return download(url);
            }
        }
    }

    public static interface Filter{

        HashSet<URL> downloadMatch(String input) throws MalformedURLException;

        HashSet<URL> enterIntoMatch(String input) throws MalformedURLException;

    }

    public static class DefaultFilter implements Filter{

        private final Pattern downloadPattern;
        private final Pattern enterIntoPattern;
        private int downloadGroupId;
        private int enterIntoGroupId;
        public DefaultFilter(Pattern downloadPattern){
            this(downloadPattern, null);
        }

        public DefaultFilter(Pattern downloadPattern, Pattern enterIntoPattern){
            this.downloadPattern = downloadPattern;
            this.enterIntoPattern = enterIntoPattern;
            downloadGroupId = 1;
            enterIntoGroupId = 1;

        }

        @Override
        public HashSet<URL> downloadMatch(String input) throws MalformedURLException {
            return urlMatch(input, downloadPattern, downloadGroupId);
        }

        @Override
        public HashSet<URL> enterIntoMatch(String input) throws MalformedURLException {
            return urlMatch(input, enterIntoPattern, enterIntoGroupId);
        }

        public HashSet<URL> urlMatch(String input, Pattern p, int groupId) throws MalformedURLException {
            HashSet<URL> result = new HashSet<>();
            if (p == null){
                return result;
            }
            Matcher matcher = p.matcher(input);
            while(matcher.find()){
                result.add(new URL(matcher.group(groupId)));
            }
            return result;
        }
    }

    public String getOutputName() {
        return outputName;
    }

    public void setOutputName(String outputName) {
        this.outputName = outputName;
    }

    public File getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(File outputDir) {
        this.outputDir = outputDir;
    }

    public AtomicInteger getOutputIndex() {
        return outputIndex;
    }

    public void setOutputIndex(AtomicInteger outputIndex) {
        this.outputIndex = outputIndex;
    }
}
