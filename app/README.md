#### Sobre o projeto

- Linguagem usada: Kotlin: devido ao curto tempo, optei pelo uso de kotlin, pois é uma linguagem menos verbosa que o java. (mas devo confessar que meu coração bate mais forte por ela hehehe)
- Injeção de dependência: pensando na comunidade kotlin o que vemos em evidência são o kodein e o koin, optei pelo uso do koin por ser de fácil aprendizado e porque já tive experiência de usa-lo em um app em produção e não tive problemas.
- RX: usei para as chamadas de API e como ele já estava no projeto usei também para interações de layout, deixando o código mais clean e facilitando a leitura.
- MVI : a arquitetura usei MVI (Model View Intention) por se tratar de uma arquitetura de programação reativa, onde cada interação do usuário a aplicação responde com um estado de tela ou um efeito, o ganho dessa arquitetura é muito significativo em aplicações onde existem objetos que devem ser atualizados conforme as interações do usuário. Em produção essa arquitetura também é muito eficaz.
- Retrofit: por ser tratar de um API para conexão com o backend, optei por ela, pois é a mais rápida.

__Tive uma "sprint" de 7 dias para fazer o projeto e não consegui concluir totalmente, então optei por entregar um MVP que fosse usável, por isso coloquei aqui uma lista de coisas que faria se na "próxima sprint", na ordem de prioridade.__

#### //TODO list:

- Implementar API do google natural languagem: cheguei a iniciar a implementação, porém esbarrei no retrofit, pois ele não faz a leitura dos ```:``` na url.
- Exibição sentimentos de cada twitter
- Testes instrumentados
- Acessibilidade
- Ktlint
- Crashlytics
- Evolução do Layout
- Animação de cada sentimento