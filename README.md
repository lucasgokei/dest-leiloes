# Dest Leilões

Site de leilões online em tempo real: frontend em Next.js (App Router) e
backend em Java (Spring Boot), com autenticação por sessão, área restrita,
lances em tempo real via WebSocket/STOMP e painel de administração.

## Stack

**Frontend** (`/`)
- **Next.js 16** (App Router, TypeScript, Tailwind CSS v4)
- **@stomp/stompjs** para receber lances/eventos de leilão em tempo real
- Sessão JWT (`jose`, verificação apenas) guardada em cookie `httpOnly`, protegendo rotas em `src/proxy.ts`

**Backend** (`backend/`)
- **Spring Boot 3** (Java 21, Maven) expondo API REST em `/api` e WebSocket/STOMP em `/ws`
- **PostgreSQL** + **Spring Data JPA** (schema versionado com **Flyway**)
- Autenticação própria: emissão/verificação de sessão em JWT (HS256), senhas com `BCryptPasswordEncoder`
- Fechamento automático dos leilões expirados via job agendado (`@Scheduled`)

## Funcionalidades

- Cadastro e login de usuários (senha com hash, sessão em cookie assinado)
- Área pública: listagem de leilões ativos e página de detalhe com contagem regressiva
- Lances em tempo real: todos que estão vendo o mesmo leilão recebem os lances instantaneamente
- Encerramento automático do leilão ao fim do prazo, com definição do vencedor (maior lance)
- Área restrita (`/dashboard`): criar leilão, ver meus leilões e meus lances
- Painel de administração (`/admin`, somente ADMIN): cancelar/excluir leilões, promover/rebaixar ou excluir usuários
- Proteção de rotas por sessão e por papel (`USER`/`ADMIN`) via `src/proxy.ts`

## Rodando localmente

### 1. Banco de dados

Suba um PostgreSQL local com Docker:

```bash
docker compose up -d
```

> O container expõe a porta **5433** do host (mapeada para a 5432 do container) porque a 5432 já estava em uso por outro Postgres nesta máquina. Se isso não for o seu caso, ajuste a porta em `docker-compose.yml` e nos `.env`.

### 2. Backend (Java)

Copie `backend/.env.example` e exporte as variáveis (ou configure na sua IDE), a `SESSION_SECRET` precisa ser **idêntica** à do frontend. Depois:

```bash
cd backend
mvn spring-boot:run
```

Isso aplica as migrações do Flyway e, em desenvolvimento (`SEED_ENABLED=true`), cria um admin, um vendedor e leilões de exemplo:

- **Admin**: `admin@destleiloes.app` / `admin1234`
- **Vendedor**: `vendedor@destleiloes.app` / `vendedor1234`

A API sobe em [http://localhost:8080](http://localhost:8080).

### 3. Frontend (Next.js)

Copie `.env.example` para `.env` (já existe um `.env` preenchido para desenvolvimento local):

```bash
npm install
npm run dev
```

Acesse [http://localhost:3000](http://localhost:3000).

### 4. Build de produção

```bash
# frontend
npm run build && npm run start

# backend
cd backend && mvn clean package && java -jar target/dest-leiloes-backend.jar
```

## Scripts úteis

- `npm run lint` — roda o ESLint no frontend
- `mvn -f backend test` — roda os testes do backend

## Estrutura principal

```
backend/src/main/java/com/destleiloes/    # arquitetura hexagonal (ports & adapters)
  domain/
    model/                      # entidades de domínio puras (User, Auction, Bid), sem JPA
    exception/                  # exceções de domínio (NotFound, Conflict, Forbidden, ...)
  application/
    port/in/                    # use cases (uma interface por operação) + commands/views
    port/out/                   # portas de saída (repositórios, hasher, publisher de eventos)
    service/                    # implementação dos use cases, orquestrando as portas de saída
  adapter/
    in/web/                     # controllers REST, DTOs de request/response, exception handler
    in/scheduler/                # fechamento automático de leilões (@Scheduled)
    out/persistence/            # entidades JPA, Spring Data repositories, mappers e adapters
    out/security/                # hasher de senha (BCrypt)
    out/messaging/               # publisher de eventos via WebSocket/STOMP
    out/bootstrap/                # seed de dados de demonstração
  config/                       # segurança, CORS, WebSocket/STOMP
backend/src/main/resources/db/migration/   # migrações Flyway
src/proxy.ts                   # proteção de rotas (login obrigatório / somente admin)
src/lib/session.ts, dal.ts     # verificação da sessão JWT + camada de autorização
src/lib/api-client.ts          # chamadas à API Java (client e server components)
src/lib/stomp-client.ts        # assinatura de eventos de leilão em tempo real
src/app/                       # páginas (App Router)
```
