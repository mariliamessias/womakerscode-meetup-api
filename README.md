<p align="center">
<img src="https://d33wubrfki0l68.cloudfront.net/3d218442b01b3bdbf82b739df4d07e450234bf9e/08a8f/assets/images/womakerscode-brand.png" height="100">
</p>
<h1 align="center">Agendados de Meetups - Projeto de Conclusão do curso</h1>
<p align="center">
</p>

<p> <i>Sobre a api: </i>
essa api é responsável por gerenciar a criação de eventos do tipo meetup. O conceito base deste projeto foi desenvolvido durante as aulas do Bootcamp de Java da Womakerscode, dentre os temas estudados em sala de aula tivemos: utilização de gradle para gerenciar as dependências, utilização da arquitetura MVC na estruturação do microsserviço, utilização de Spring JPA, desenvolvimento de testes Unitários, utilização de azure para hospedar a aplicação. 
 </p>  
 
 <p><i>Ocorreram alguns ajustes e implementações que entendi que seriam interessantes de serem estudados e colocados em prática com esse projeto além do que foi pedido em sala de aula.</i></p>
 
 <h4>Cenário</h4>
 
 Eu como usuário desse sistema, gostaria de realizar um registro em um meetup em um site com as seguintes características:
  <ul>
  <li>Só conseguir realizar a solicitação caso eu tenha um usuário válido;</li>
  <li>Só conseguir realizar a solicitação caso eu tenha um token de acesso válido;</li>
  <li>Quero que meus dados sejam persistidos em uma base de dados e não sejam perdidos quando a aplicação for desligada (por exemplo);</li>
  <li>Quero receber um email de confirmação caso o meu registro tenha sido realizado com sucesso;</li>
  <li>Não vejo problema nenhum em não receber um email na hora, desde que isso não implique no meu registro no meetup;</li>
  <li>Quero conseguir saber em quais meetups estou cadastrado(a);</li>
  <li>Quero ser irnformado quando eu tentar entrar um meetup que já esteja cheio;</li>
  <li>Quero ser informado quando eu tentar entrar em um meetup que já foi cancelado.</li>
  </ul>
 
 
  
<h4>Soluções Desenvolvidas</h4>

 <ul>
<li>Utilização de banco de dados MySQL para armazenar os dados produtivos e H2 para o ambiente de testes. Para esse cenário precisei configurar um arquivo application dedicado aos testes para que não utilizassem os valores produtivos.</li>
   </br>

<li>Utilização das configurações da azure para hospedar as variáveis que continham informações sensíveis. Configurei uma environment para cada cenário em todos os microsserviços do fluxo. Com isso, não ficam públicas as informações no github.</li>
 
  </br>
<li>Utilização de um servidor de autorização. Para que eu pudesse simular como seria realmente um fluxo completo de um usuário realizar uma inscrição em um meetup, pensei em implementar um microserviço que é responsável por fazer o gerenciamento da autorização dos recursos que serão utilizados no fluxo, gerando assim um token que será consumido pelos clientes.</li>
  </br>

 <li>Utilização de um sistema assíncrono para envio de emails no caso de confirmação em registro de um usuário em um meetup. Pensei em implementar um sistema assíncrono para garantir uma boa experiência do usuário no fluxo, não impedindo que ele possa se inscrever no meetup caso o serviço de envio de emaisl esteja fora. <b>A tecnologia utilizada foi RabbitMQ.</b></li>
  
 </br>
<li>Conforme mencionado anteriormente, foi pensado em uma forma de envio de uma comunicação para o usuário no momento em que ele se registra em um evento. Para isso desenvolvi um microsserviço que segmenta essa responsabilidade de escutar a fila onde as mensagens foram postadas no fluxo anterior.</li>

<br>
<li>Desenvolvi dois endpoins para atender os cenários citados acima, que serão documentados mais abaixo nesta documentação.</li>
</ul>

 <h4>Bibliotecas utilizadas no desenvolvimento:</h4>
 
```bash

  spring-boot-starter-data-jdbc
  spring-boot-starter-web
  h2database
  spring-boot-starter-test
  junit-jupiter-api
  dbunit
  ider-core
  spring-test-dbunit
```

 <h4>Diagrama genérico das partes da aplicação:</h4>
 <img src="./meetup.drawio.png">

 
  </br>
 <h4>Requests e Responses dos endpoints da aplicação:</h4>
 
  <h4>Diagrama genérico das partes da aplicação:</h4>
 <img src="./swagger.png">


 <h5>Criação de um novo Jedi:</h5>
 
 ```bash
 Request:

 curl --location --request POST 'localhost:8080/jedi' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "teste",
    "strength": 123
}'

Response: 200 - OK

{
    "id": 1,
    "name": "teste",
    "strength": 123,
    "version": 1
}

```

<h5>Busca de Jedi por Id:</h5>
 
 ```bash
 Request:

curl --location --request GET 'localhost:8080/jedi/1' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "teste",
    "strength": 123
}'

Response: 201 - Created

{
    "id": 1,
    "name": "teste",
    "strength": 123,
    "version": 1
}

```

<h5>Alteração nos dados do Jedi:</h5>
 
 ```bash
 Request:

curl --location --request PUT 'localhost:8080/jedi/1' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "teste 2",
    "strength": 123
}'

Response: 204 - No Content

```

<h5>Remoção nos dados do Jedi:</h5>
 
 ```bash
 Request:

curl --location --request DELETE 'localhost:8080/jedi/1'

Response: 204 - No Content

```
