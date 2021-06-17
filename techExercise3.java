import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Scanner;


public class techExercise3 {
    public static void main(String[] args) {

        ArrayList<String> a = new ArrayList<String>();
	System.out.println("This is kennedy's edit");
        Scanner scan = new Scanner(System.in);
        System.out.println("What website would you like to web scrape?");
        String input = scan.next();
        System.out.println("How deep would you like to scrape?");
        int deep = scan.nextInt();
        //Website to web crawl
        String url = input;
        String answer;
        do {
            //Recursive crawl function
            crawl(deep, url, a);

            System.out.println("What link number would you like to change?");
            int newChange = scan.nextInt();
            System.out.println("What link number would you like to replace it with?");
            int newChange2 = scan.nextInt();

            System.out.println("Old link at level " + newChange + ": " + a.get(newChange));
            a.set(newChange, a.get(newChange2));
            System.out.println("New link at level " + newChange2 + ": " + a.get(newChange));

            System.out.println("Would you like to web scrape again with the new link? Answer 'yes' or 'no'");
            answer = scan.next();
        }
        while (!answer.equals("no"));

        System.out.println("Exiting");

    }

    /* Recursive function to crawl the website
    level - how many layers deep we will go into the website before restarting the recursive function
    url - what url to visit
    visited - keeps track of websites that have been visited so you don't visit duplicates
     */
    private static void crawl(int level, String url, ArrayList<String> visited) {

        //Will only go through 1 layers of websites max in each link
        if (level <= 1) {

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
