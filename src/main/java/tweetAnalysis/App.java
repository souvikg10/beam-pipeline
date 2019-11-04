package tweetAnalysis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Count;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.Filter;
import org.apache.beam.sdk.transforms.FlatMapElements;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.TypeDescriptors;
import tweetAnalysis.tweetModel;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {

        // Create a PipelineOptions object. This object lets us set various execution
        // options for our pipeline, such as the runner you wish to use. This example
        // will run with the DirectRunner by default, based on the class path configured
        // in its dependencies.
        PipelineOptions options = PipelineOptionsFactory.create();

        Pipeline p = Pipeline.create(options);
        PCollection<String> tweetobj = p.apply("Read Tweets", TextIO.read().from("data/Donald-Tweets!small.csv"));
        PCollection<String> tweetobj1 = tweetobj.apply("Remove empty lines",
                Filter.by((String word) -> !word.isEmpty()));
        PCollection<tweetModel> tweets = tweetobj1.apply("LoadTweetstoTable", // the transform name
                ParDo.of(new DoFn<String, tweetModel>() { // a DoFn as an anonymous inner class instance
                    /**
                     *
                     */
                    private static final long serialVersionUID = 1L;

                    @ProcessElement
                    public void processElement(@Element String line, OutputReceiver<tweetModel> out) {
                        String[] strArr = line.split(",");
                        tweetModel tm = new tweetModel(0, "");
                        try {
                            tm.setTweet(strArr[2]);
                            
                        } catch (ArrayIndexOutOfBoundsException e) {
                            tm.setTweet("no tweet");
                        }

                        out.output(tm);
                    }
                }));
        PCollection<String[]> words = tweets.apply("Create Tokens", // the transform name
                ParDo.of(new DoFn<tweetModel, String[]>() { // a DoFn as an anonymous inner class instance
                    /**
                     *
                     */
                    private static final long serialVersionUID = 1L;

                    @ProcessElement
                    public void processElement(@Element tweetModel tweet, OutputReceiver<String[]> out) {
                        String[] strArr = tweet.getTweet().split("[^\\p{L}]+");
                        out.output(strArr);
                    }
                }));
        PCollection<String> words1 = words.apply("Remove Stopwords", // the transform name
                ParDo.of(new DoFn<String[], String>() { // a DoFn as an anonymous inner class instance
                    /**
                     *
                     */
                    private static final long serialVersionUID = 1L;
                    String ar;
                    @ProcessElement
                    public void processElement(@Element String[] tokens, OutputReceiver<String> out) {
                        try {
                            List<String> stopwords = Files.readAllLines(Paths.get("data/english_stopwords.txt"));
                            for(String word : tokens) {
                                if(!stopwords.contains(word)) {
                                    ar += " ";
                                    ar += word;    
                                }
                            }
                        } catch (IOException e) {
                            
                            e.printStackTrace();
                        }
                        out.output(ar);
            
      }
    }));
    PCollection<String> words2 = words1.apply(FlatMapElements.via(
        new SimpleFunction<String, List<String>>() {
                    /**
                     *
                     */
                    private static final long serialVersionUID = 1L;

                    public List<String> apply(String line) {
                    return Arrays.asList(line.split(" "));
          }
        }));
    PCollection<KV<String, Long>> count = words2.apply(Count.perElement());
    
    count.apply(
            MapElements.into(TypeDescriptors.strings())
                .via(
                    (KV<String, Long> wordCount) ->
                        wordCount.getKey() + ": " + wordCount.getValue()))
        // Concept #4: Apply a write transform, TextIO.Write, at the end of the pipeline.
        // TextIO.Write writes the contents of a PCollection (in this case, our PCollection of
        // formatted strings) to a series of text files.
        //
        // By default, it will write to a set of files with names like wordcounts-00001-of-00005
        .apply(TextIO.write().to("data/wordcounts").withSuffix(".csv"));
      
          
      
      p.run().waitUntilFinish(); 
      
    }
  }
  
