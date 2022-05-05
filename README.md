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

 <h4>Visão geral da arquitetura aplicação:</h4>
 <img src="./meetup.drawio.png">
<i>Legenda:</i> de uma maneira resumida, temos o cliente acessando a nossa aplicação através de um client front-end, esse por sua vez utiliza os recursos do microsserviço auth-api para adquirir um token, validar o token ou criar um novo usuário. Após esse processo, é permitido que o client front-end faça uma requisição via token a aplicação backend, cujo nome é meetup-api. A aplicação meetup-api tem acesso a realizar a publicação de uma mensagem na fila que será consumida via notifications-api, que se encarregará de atributir os valores para o template HTML e disparar o email para o cliente.
 
 </br>
  <h4>Sobre as apis:</h4>
  
<b>notifications-api: [https://github.com/mariliamessias/womakerscode-notifcations-api]</b> api responsável por escutar as mensagens na fila de publicação de meetups e enviar um email para o usuário. Segue abaixo exemplo de email enviado para um usuário no momento de cadastro no evento com sucesso:
  
  <img src="./email.png">

<b>auth-api: [https://github.com/mariliamessias/womakerscode-auth-api] </b> responsável por armazenar os clients e seus respectivos parâmetros, o que implica em um gerenciamento de quais clients podem adquirir acesso a seus recursos. Além disso, é responsável também por armazenar os usuários do nosso fluxo, bem como o gerenciar o seu token de acesso.
  
<b>meetup-api</b>: nesta api temos basicamente quatro grandes responsabilidades: cadastro de eventos, de registros, de pessoas e a publicação de mensagens em um exchange em caso de sucesso de registro de um usuário em um evento. Nessa api existe uma configuração para validação das requests na api auth-api, garantindo a segurança dentro do fluxo, cada request que requer autorização, precisa ser validada junto ao servidor.
  
  
 <h4>Rotas da aplicação - MEETUP</h4>
  <img src="./swagger.png">
  
  <i>Event</i>
  
  GET - /events?status={status} </br>
  
  <i>Request:</i>
  
  ````
  curl --location --request GET 'localhost:9191/events?status=FULL' \
--header 'Authorization: Bearer 659f90ca-9acd-4fe5-a1a8-XXXX'
  ````
  
  <i>Response:</i>

  ````
  [
    {
        "id": 1,
        "name": "novas features java 17",
        "status": "FULL",
        "registrations": [
            {
                "id": 1,
                "status": "CREATED",
                "description": "convite estudante",
                "created_at": "2022-05-02T06:41:41",
                "user_name": "{USERNAME)",
                "event_name": "novas features java 17"
            },
            {
                "id": 2,
                "status": "CREATED",
                "description": "convite estudante",
                "created_at": "2022-05-02T06:42:30",
                "user_name": "{USERNAME}",
                "event_name": "novas features java 17"
            },
            {
                "id": 3,
                "status": "CREATED",
                "description": "convite estudante",
                "created_at": "2022-05-02T06:44:31",
                "user_name": "{USERNAME}",
                "event_name": "novas features java 17"
            }
        ],
        "created_at": "2022-05-02T06:41:13",
        "maximun_spots": 3,
        "alocated_spots": 3
    }
]
  ````
  GET - /events/{id} </br>
  
  <i>Request:</i>
  ````
  curl --location --request GET 'localhost:9191/events/2' \
--header 'Authorization: Bearer 659f90ca-9acd-4fe5-a1a8-XXX' \
  ````
  
  <i>Response:</i>
  
  ````
  {
    "id": 2,
    "name": "novas features java 17",
    "status": "CREATED",
    "registrations": [
        {
            "id": 4,
            "status": "CREATED",
            "description": "convite estudante",
            "created_at": "2022-05-03T13:05:29",
            "user_name": "{USERNAME}",
            "event_name": "novas features java 17"
        },
        {
            "id": 5,
            "status": "CREATED",
            "description": "convite estudante",
            "created_at": "2022-05-03T17:28:41",
            "user_name": "{USERNAME}",
            "event_name": "novas features java 17"
        }
    ],
    "created_at": "2022-05-03T13:05:13",
    "maximun_spots": 3,
    "alocated_spots": 2
}
  ````
  
  POST - /events </br>
  
  <i>Request: </i>
  ````
  curl --location --request POST 'localhost:9191/events' \
--header 'Authorization: Bearer 659f90ca-9acd-4fe5-a1a8-XXXX' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "novas features java 17",
    "maximun_spots": 3,
    "alocated_spots": 0,
    "event_date": "2022-06-01"
}'
  ````
 <i>Response: </i>
    
   ````
   {
   "id": 3,
   "name": "novas features java 17",
   "status": "CREATED",
   "registrations": [],
   "created_at": "2022-05-03T13:09:17.335272",
   "maximun_spots": 3,
   "alocated_spots": 0
}
   ````
  
  PUT - /events/{id} </br>
<i>Request: </i>

 ````
 curl --location --request PUT 'localhost:9191/events/1' \
--header 'Authorization: Bearer 659f90ca-9acd-4fe5-a1a8-XXX' \
--header 'Content-Type: application/json' \
--data-raw '{
    "maximun_spots": 3
}'
 ````

<i>Response: </i>

````
{
    "id": 1,
    "name": "novas features java 17",
    "status": "FULL",
    "registrations": [
        {
            "id": 1,
            "status": "CREATED",
            "description": "convite estudante",
            "created_at": "2022-05-02T06:41:41",
            "user_name": "{USERNAME}",
            "event_name": "novas features java 17"
        },
        {
            "id": 2,
            "status": "CREATED",
            "description": "convite estudante",
            "created_at": "2022-05-02T06:42:30",
            "user_name": "{USERNAME}",
            "event_name": "novas features java 17"
        },
        {
            "id": 3,
            "status": "CREATED",
            "description": "convite estudante",
            "created_at": "2022-05-02T06:44:31",
            "user_name": "{USERNAME}",
            "event_name": "novas features java 17"
        }
    ],
    "created_at": "2022-05-02T06:41:13",
    "maximun_spots": 3,
    "alocated_spots": 3
}
````
  DELETE - /events/{id} </br>
<i>Request: </i>
    
   ````
   curl --location --request DELETE 'localhost:9191/events/4' \
--header 'Authorization: Bearer 659f90ca-9acd-4fe5-a1a8-XXX' 
   ````
   
<i>Response: </i> 204 - NO CONTENT
    
  PUT - /events/{id}/cancel </br>
 <i>Request: </i>
  ````
  curl --location --request PUT 'localhost:9191/events/3/cancel' \
--header 'Authorization: Bearer 659f90ca-9acd-4fe5-a1a8-xxxx' \
--data-raw ''
  ````
 <i>Response: </i>
 ````
 {
    "id": 3,
    "name": "novas features java 17",
    "status": "CANCELED",
    "registrations": [],
    "created_at": "2022-05-03T13:09:17",
    "maximun_spots": 3,
    "alocated_spots": 0
}
 ````
 

  <i>Person</i>
  
  GET - /people/{id} </br>
  
  <i>Request:</i>
  ````
curl --location --request GET 'localhost:9191/people/1' \
--header 'Authorization: Bearer 659f90ca-9acd-4fe5-a1a8-XXX'\
  ````
  <i>Response:</i>
  
  ````
  {
    "id": 1,
     "name": "{NAME}",
     "email": "{EMAIL}",
     "birth_date": "YYYY-MM-DD",
     "username": "{USERNAME}",
     "address": {
         "public_place": "test place",
         "number": 123,
         "zip_code": "45646",
         "neighborhood": "test",
         "city": "test city",
         "country": "test country"
     }
    "created_at": "2022-05-02T06:41:29"
}
  ````
  
  POST - /people </br>
  
   <i>Request:</i>
  ````
  curl --location --request POST 'localhost:9191/people' \
--header 'Authorization: Bearer 659f90ca-9acd-4fe5-a1a8-XXXX' \
--header 'Content-Type: application/json'\
--data-raw '{
        "name": "{NAME}",
        "email": "{EMAIL}",
        "birth_date": "YYYY-MM-DD",
        "username": "{USERNAME}",
        "address": {
            "public_place": "test place",
            "number": 123,
            "zip_code": "45646",
            "neighborhood": "test",
            "city": "test city",
            "country": "test country"
        }
}'
  
  ````
  
   <i>Response:</i>
  
  ````
  {
    "id": 1,
     "name": "{NAME}",
     "email": "{EMAIL}",
     "birth_date": "YYYY-MM-DD",
     "username": "{USERNAME}",
     "address": {
         "public_place": "test place",
         "number": 123,
         "zip_code": "45646",
         "neighborhood": "test",
         "city": "test city",
         "country": "test country"
     }
    "created_at": "2022-05-02T06:41:29"
}
  ````
  
  <i>Registration</i>
  
  GET - /registrations/users?user_name={username} </br>
  GET - /registrations/{id} </br>
  POST - /registrations </br>
  PUT - /registrations/{id} </br>
  DELETE - /registrations/{id} </br>
  
<h4>Rotas da aplicação - AUTH-API</h4>
Link de acesso a documentação da api: [https://github.com/mariliamessias/womakerscode-auth-api]


<h4>Rotas da aplicação - NOTIFICATIONS-API</h4>
Link de acesso a documentação da api: [https://github.com/mariliamessias/womakerscode-notifcations-api]

 <h4>Bibliotecas utilizadas no desenvolvimento dos três projetos:</h4>
 
 
```bash

  spring-boot-starter-web
  spring-boot-starter-test
  spring-cloud-starter-oauth2
  spring-boot-starter-validation
  spring-boot-starter-data-jpa
  spring-boot-starter-mail
  spring-boot-starter-amqp
  spring-boot-starter-thymeleaf
  passay
  junit-jupiter-api
  spring-secutiry
  mysql-connector
  h2database
  lombok
  spring-rabbit-test
  azurewebapp
  dbunit
  ider-core
  spring-test-dbunit
```

 <h4>Próximos Passos:</h4>
 
- Adicionar testes nas apis auth-api e notifications-api;
- Desenvolver o projeto front-end para se comunicar com os serviços;
- Adicionar um endpoint para criação de clients na auth-api;
- Adicionar um template de envio de email em caso o evento seja cancelado.

