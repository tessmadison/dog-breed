package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();



    @Override
    public List<String> getSubBreeds(String breed) {
        // TODO Task 1: Complete this method based on its provided documentation
        //      and the documentation for the dog.ceo API. You may find it helpful
        //      to refer to the examples of using OkHttpClient from the last lab,
        //      as well as the code for parsing JSON responses.
        // return statement included so that the starter code can compile and run.

        String url = String.format("https://dog.ceo/api/breed/%s/list", breed);
        final Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            final Response response = client.newCall(request).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getString("status").equalsIgnoreCase("success")) {
                final JSONArray subBreedsArray = responseBody.getJSONArray("message");
                List<String> subBreeds = new ArrayList<>();

                for (int i = 0; i < subBreedsArray.length(); i++) {
                    subBreeds.add(subBreedsArray.getString(i));
                }

                return subBreeds;
            }

            else {
                throw new BreedNotFoundException("Couldn't find breed: " + breed);
            }
        }
        catch (IOException | RuntimeException event) {
            throw new BreedNotFoundException("Couldn't fetch sub-breeds for breed: " + breed + ", " + event);
        }
    }
}

