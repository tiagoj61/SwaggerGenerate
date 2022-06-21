
<h1 align="center"> Swagger Generator for JAX-RS</h1>
<p align="center">
<img src="http://img.shields.io/static/v1?label=STATUS&message=v1.0.0%20FINISHED&color=GREEN&style=for-the-badge"/>
</p>

<h4 align="center"> 
    :construction:  Construction  :construction:
</h4>

## Table of content

* [📚 Description](#-description)
* [:hammer: How to use](#hammer-how-to-use)
* [☔ Dictionary of tags](#-dictionary-of-tags)
* [☄ Version history](#-version-history)
* [✔️ Technologies used](#%EF%B8%8F-technologies-used)

## 📚 Description
<p> The main objective of this API is to generate a swagger doc automatically for REST API coded by Java with JAX-RS, but for this, we need to follow some patterns defined in this text</p>

## :hammer: How to use
<p>To use this API we need to follow three simple steps</p>

- `Step 1` Import JAR

  - We need to download the JAR in the last branch tag 
  - Create a folder, side by side with your other dependencies, named tiago, and paste the JAR inside
  - Finally, add it to your maven project like this
    ```
    <dependency>
      <groupId>tiago</groupId>
      <artifactId>SwaggerAutomate</artifactId>
      <version>0.0.1-SNAPSHOT</version>
    </dependency>
    ```

- `Step 2` Adequate packages

  - We need a package to group our code, inside this package, to our methods, it needs an interface to create the method and with some tags whose Will be used to generate our swagger That's the main change in development lets to some examples
  - Exemple:
    ``` 
    - restful (Package)
      - IExempleRest.java (Class)
      - implements (Package)
        - ExempleRest.java (Class)
    ```   
    
- `Step 3` Usage
  - `Interface`

      - In IExempleRest.java
        ``` 
        public interface RestTesteInterface {

        @Tag(value = "Teste")
        @ReturnsCods(value = { @CodeAndReturn(code = 200, object = ReflectionHelper2.class),
            @CodeAndReturn(code = 403, object = ErroMensage.class) })
        public Response restTeste(ReflectionHelper obj);

        @Tag(value = "Teste")
        @ReturnsCods(value = { @CodeAndReturn(code = 200)})
        public Response restTeste2(List<ReflectionHelper> objArr);

        }
        ``` 
        
      - The first tag (’Tag’) is used to a group in the swagger file, generate the ‘tags’, for each different name a new group is created
      - In addition, of the ‘Tag’, we only need one more in an interface, ‘ReturnsCods’ this tag spectates an array of another tag ‘CodeAndReturn’ where we need to indicate the code that can be returned and the object if the return is a simples int or double we need to indicate the object who represents this primitive type, in this case, Integer or Double.
      - As said before, the ‘ReturnsCods’ spectate an array so we can add multiples ‘CodeAndReturn’ for each API return.
      - The last thing that we need to construct the interface how is common, showing who classes are needed to receive.

  - `Main method`

      - The other information needed will be retrieved here
      - In ExempleRest.java
        ``` 
        @Component
        @Path("/usuario")
        public class RestTeste implements RestTesteInterface {

          @Path("login")
          @POST
          @Consumes(MediaType.APPLICATION_JSON)
          @Produces(MediaType.APPLICATION_JSON)
          public Response restTeste(ReflectionHelper obj) {
            try {
              List<String> array = new ArrayList<>();
              return Response.ok().entity(new String()).build();
            } catch (Exception e) {
              e.printStackTrace();
              return Response.serverError().build();
            }
          }

          @Path("receberDadosRecipientes")
          @POST
          @Consumes(MediaType.APPLICATION_JSON)
          @Produces(MediaType.APPLICATION_JSON)
          public Response restTeste2(List<ReflectionHelper> id) {
            try {

              return Response.ok().build();
            } catch (Exception e) {
              e.printStackTrace();
              return Response.serverError().build();
            }
          }
        }
        ```  
    - The HTTP method will be retrieved from here by each method the type of consumption and produce too, at last, the path for each method and for the class will be retrieved from here, of course, we need to implement the interface of this class

## ☔ Dictionary of tags

- `@Tag`
    - Used to group the methods in the Swagger file

## ☄ Version history

- ``v1.0.0``
    - This version accepts the following HTTP methods
        -  POST, GET
    - Includes these tags
        - Tag   

## ✔️ Technologies used

- ``Maven 3.8.6``
- ``Java 1.8``
