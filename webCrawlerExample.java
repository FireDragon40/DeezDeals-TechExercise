import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.util.ArrayList;
import java.io.IOException;

public class webCrawlerTest {
    public static void main(String[] args) {

        //Website to web crawl
        String url = "https://en.wikipedia.org/";

        //Recursive crawl function
        crawl(1, url, new ArrayList<String>());
    }

    /* Recursive function to crawl the website
    level - how many layers deep we will go into the website before restarting the recursive function
    url - what url to visit
    visited - keeps track of websites that have been visited so you don't visit duplicates
     */
    private static void crawl(int level, String url, ArrayList<String> visited) {

        //Will only go through 5 layers of websites max in each link
        if (level <= 5) {

            //Calls Document help function to attempt connection to link
            Document doc = request(url, visited);

            //If document is successfully connected, we will find all the links on the document
            if (doc != null) {

                //Looks through the document for links
                for(Element link : doc.select("a[href]"))
                {

                    //Adds link to a new variable
                    String next_link = link.absUrl("href");

                    //Check if the link has been visited yet. If not, crawl in that link
                    if(visited.contains(next_link) == false)
                    {
                        crawl(level++, next_link, visited);
                    }
                }
            }
        }
    }

    /* Helper function to request access to the link
    url - link being requested access
    v - keeps track of visited links
     */
    private static Document request(String url, ArrayList<String> v) {
        try {

            //Requests access to the link with Jsoup "Connection" data type
            Connection con = Jsoup.connect(url);

            //Gets the document from the connected link
            Document doc = con.get();

            //Checks if connection request was successful
            if(con.response().statusCode() == 200) {

                //If successful, returns link of site and document title
                System.out.println("Link: " + url);
                System.out.println(doc.title());

                //Adds visited link to our Arraylist
                v.add(url);

                //Returns the document
                return doc;
            }

            //Returns null at end. If link is not successfully accessed (ie. does not return 200 status code, this function will only return null.
            return null;
        }

        //Catches IOException to return null Document
        catch(IOException e) {
            return null;
        }
    }
}
