
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

public class FoodHub {

    public static void main(String[] args) throws IOException {

       int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8081"));
HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", port), 0);
        

        server.createContext("/", exchange -> {

            String path = exchange.getRequestURI().getPath();

            if (path.equals("/recipe")) {
                showRecipe(exchange);
            } else if (path.equals("/style.css")) {
                sendFile(exchange, "style.css", "text/css");
            } else if (path.equals("/")) {
                sendFile(exchange, "index.html", "text/html");
            } else {
                sendResponse(exchange, 404, "Page not found", "text/plain");
            }

        });

        server.start();

        System.out.println("FoodHub server is running!");
        System.out.println("Open http://localhost:8081 in your browser.");
    }

    public static void sendFile(
        HttpExchange exchange, String fileName, String contentType
    ) throws IOException {

        Path file = Path.of(fileName);

        if (!Files.exists(file)) {
            file = Path.of("..", fileName);
        }

        if (!Files.exists(file)) {
            sendResponse(exchange, 404, fileName + " not found", "text/plain");
            return;
        }

        String content = Files.readString(file, StandardCharsets.UTF_8);
        sendResponse(exchange, 200, content, contentType);
    }

    public static void showRecipe(HttpExchange exchange)
        throws IOException {

        String query = exchange.getRequestURI().getQuery();

        String recipe = "";

        if (query != null && query.equals("id=1")) {
            recipe = getRecipe(
                "Veg Fried Rice",
                "Rice, Carrot, Beans, Onion, Capsicum, Garlic, Soy Sauce",
                "1. Wash the rice thoroughly and cook it until the grains are soft but not mushy. Allow it to cool completely.<br><br>" +
                "2. Wash and finely chop the carrots, beans, capsicum and onions into small, even-sized pieces.<br><br>" +
                "3. Heat two tablespoons of oil in a large pan. Add chopped garlic and saute for a few seconds until fragrant.<br><br>" +
                "4. Add the chopped vegetables and stir-fry them on high heat for 3-4 minutes until they are cooked but slightly crunchy.<br><br>" +
                "5. Add soy sauce, pepper and salt according to your taste. Mix everything properly.<br><br>" +
                "6. Add the cooled rice and gently mix it with the vegetables without breaking the rice grains.<br><br>" +
                "7. Stir-fry everything together for another 2-3 minutes until the rice is evenly coated with the sauces.<br><br>" +
                "8. Turn off the heat, garnish with chopped spring onions and serve hot."
            );

        } else if (query != null && query.equals("id=2")) {
            recipe = getRecipe(
                "Paneer Butter Masala",
                "Paneer, Tomato, Onion, Butter, Cream, Ginger, Garlic, Spices",
                "1. Cut the paneer into medium-sized cubes and keep them aside. Wash and chop the tomatoes and onions into small pieces.<br><br>" +
                "2. Heat butter in a pan. Add chopped onions, ginger and garlic. Saute until the onions become soft and slightly golden.<br><br>" +
                "3. Add chopped tomatoes, salt and a little water. Cook until the tomatoes become soft and completely blended.<br><br>" +
                "4. Allow the mixture to cool slightly. Grind it into a smooth paste using a mixer. This creates the base for the gravy.<br><br>" +
                "5. Heat some butter in the same pan. Add the prepared tomato-onion paste along with turmeric, chilli powder, garam masala and other spices.<br><br>" +
                "6. Cook the gravy on medium heat for a few minutes, stirring occasionally. Add a little water if the gravy becomes too thick.<br><br>" +
                "7. Add the paneer cubes and gently mix them with the gravy. Cook for another 3-4 minutes so that the paneer absorbs the flavours.<br><br>" +
                "8. Add fresh cream and mix gently. Allow it to simmer briefly until the gravy becomes rich and creamy.<br><br>" +
                "9. Turn off the heat, garnish with fresh coriander leaves and serve hot with naan, roti or rice."
            );

        } else if (query != null && query.equals("id=3")) {
            recipe = getRecipe(
                "Chicken Biryani",
                "Chicken, Basmati Rice, Onion, Tomato, Yogurt, Ginger, Garlic, Biryani Spices",
                "1. Wash the basmati rice thoroughly and soak it in water for approximately 20-30 minutes. Drain the water before cooking.<br><br>" +
                "2. Clean the chicken pieces properly. In a bowl, combine chicken with yogurt, ginger-garlic paste, turmeric, chilli powder, garam masala and salt.<br><br>" +
                "3. Mix the chicken thoroughly with the spices and allow it to marinate for at least 30 minutes so that it absorbs the flavours.<br><br>" +
                "4. Heat oil or ghee in a large cooking pot. Add sliced onions and fry until they turn golden brown. Keep some fried onions aside for garnishing.<br><br>" +
                "5. Add chopped tomatoes and the marinated chicken to the pot. Mix well and cook until the tomatoes soften and the chicken is partially cooked.<br><br>" +
                "6. Add mint leaves, coriander leaves and the required biryani spices. Stir everything together and cook until the chicken is tender and the masala becomes thick.<br><br>" +
                "7. In a separate pot, boil water with salt and whole spices. Add the soaked rice and cook until it is approximately 70 percent cooked. Drain the excess water.<br><br>" +
                "8. Spread the partially cooked rice evenly over the chicken masala. Add fried onions, mint leaves, coriander leaves and a little ghee on top.<br><br>" +
                "9. Cover the pot tightly and cook on low heat for approximately 15-20 minutes, until the chicken and rice are completely cooked and the flavours combine.<br><br>" +
                "10. Turn off the heat and allow the biryani to rest for a few minutes. Gently mix before serving and enjoy it hot with raita."
            );
        }

        if (recipe.isEmpty()) {
            sendResponse(exchange, 404, "Recipe not found", "text/plain");
            return;
        }

        String page =
            "<!DOCTYPE html>" +
            "<html lang='en'>" +
            "<head>" +
            "<meta charset='UTF-8'>" +
            "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
            "<title>FoodHub Recipe</title>" +
            "<link rel='stylesheet' href='/style.css'>" +
            "</head>" +
            "<body>" +
            "<section class='recipes'>" +
            recipe +
            "<p style='margin-top:30px;'>" +
            "<a href='/' class='explore-btn'>BACK TO FOODHUB</a>" +
            "</p>" +
            "</section>" +
            "</body>" +
            "</html>";

        sendResponse(exchange, 200, page, "text/html");
    }

    public static String getRecipe(
        String name, String ingredients, String instructions
    ) {

        return "<div class='about-content'>" +
            "<p class='section-label'>FOODHUB RECIPE</p>" +
            "<h2>" + name + "</h2>" +
            "<h3 style='color:#e8c65a; margin:25px 0 10px;'>Ingredients</h3>" +
            "<p>" + ingredients + "</p>" +
            "<h3 style='color:#e8c65a; margin:25px 0 10px;'>Preparation Instructions</h3>" +
            "<p style='text-align:left;'>" + instructions + "</p>" +
            "</div>";
    }

    public static void sendResponse(
        HttpExchange exchange, int status, String response, String contentType
    ) throws IOException {

        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set(
            "Content-Type", contentType + "; charset=UTF-8"
        );

        exchange.sendResponseHeaders(status, bytes.length);

        exchange.getResponseBody().write(bytes);
        exchange.close();
    }
}
