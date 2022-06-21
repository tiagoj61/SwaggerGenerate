
<h1 align="center"> Swagger Generator for JAX-RS</h1>
<p align="center">
<img src="http://img.shields.io/static/v1?label=STATUS&message=v1.0.0%20FINISHED&color=GREEN&style=for-the-badge"/>
</p>

<h4 align="center"> 
    :construction:  Construction  :construction:
</h4>

## Table of content

* [Description](#description)
* [:hammer: Funcionalidades do projeto](#hammer-funcionalidades-do-projeto)
* [ 🛠️ Rodando o projeto](#%EF%B8%8F-rodando-o-projeto)
* [☔ Testes](#-testes)
* [✔️ Tecnologias utilizadas](#%EF%B8%8F-tecnologias-utilizadas)

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

## 🛠️ Rodando o projeto

Partindo do pressuposto que o java e o maven já encontram se instalados, além disso a porta 8080 não pode estar em uso,
para rodar o projeto é necessário baixa-lo,
```
git clone https://github.com/tiagoj61/SpringMatriz.git
``` 
navegar até a pasta do projeto e rodar o seguinte comando:
```
mvn spring-boot:run
```
assim será possível utilizar as funcionalidades descritas acima.

Ainda é possível rodar o projeto fazendo o download do zip e rodando através do comando:
```
mvnw spring-boot:run
```

## ☔ Testes

Os teste do sistema são executados através do comando ‘mvn clean install’ e a resposta é exibida no console.

O sistema abrange uma gama de testes unitários tão grande quanto possível desde valores null até matrizes de tamanhos 100x100, porém sempre é possível que existam teste não mapeados, no package de testes é possível encontrar os teste que abrangem o package de service, facada e controller, que são os principais pacotes de funcionalidades da aplicação.

Assim foram mapeado de forma geral duas exceptions para os cenários fora do esperado no sistema, são eles:

  NonQuadraticMatrixException.java

    → Para cenários onde a matriz existe mais não quadrática, logo não é possível realizar a rotação nela.

  MatrixBadRequestException.java
  
    → Para cenários onde o objeto não atende ao requisito de ser um array

Além disso para realização dos testes foi criado uma classe auxiliar, MatrixHelper.java, para realizar a criação de matrizes e arrays de forma automática e dinâmica, sendo necessário apenas informar o tamanho, também foram criados métodos para exibição da matriz no console de teste, porém não recomendado, foi utilizado apenas para confirmação visual mas removido dos testes.


## ✔️ Tecnologias utilizadas

- ``Maven 3.8.6``
- ``Java 11``
- ``Spring Boot``
- ``Junit``
- ``Spring Fox``
