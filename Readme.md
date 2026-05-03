# DacdTrabajo — Aplicación de Datos de Fútbol y Meteorología

## Objetivo de la Funcionalidad de Negocio

Esta aplicación combina datos de partidos de fútbol de LaLiga con datos meteorológicos de las ciudades donde se juegan los partidos. El objetivo es proporcionar al usuario final información útil como:

- Ver los partidos jugados en una ciudad con las condiciones meteorológicas del momento
- Consultar todos los partidos de un equipo con el tiempo que hacía
- Ver el tiempo actual de cada ciudad de LaLiga
- Recibir alertas de partidos jugados bajo la lluvia

## Arquitectura Final del Sistema

Feeder A (football-module) ──────┐
├──► ActiveMQ Broker ──► Event Store Builder ──► eventstore/
Feeder B (weather-module) ───────┘         │
│
└──► Business Unit ──► Datamart (SQLite) ──► API REST

### Módulos

- **football-module** — Consume la API de football-data.org y publica eventos en el topic `Football`
- **weather-module** — Consume la API de OpenWeatherMap y publica eventos en el topic `Weather`
- **event-store-builder** — Suscrito a los topics, almacena eventos en archivos `.events`
- **business-unit** — Consume eventos en tiempo real, carga histórico y expone una API REST

## Cómo Ejecutar Cada Componente

### 1. Arrancar ActiveMQ

```bash
cd C:\ruta\a\activemq\bin
activemq.bat start
```



Consola web disponible en: `http://127.0.0.1:8161` (usuario: `admin`, contraseña: `admin`)

### 2. Ejecutar Event Store Builder


Program arguments: tcp://localhost:61616 
C:\Users\gabri\IdeaProjects\DacdTrabajo\eventstore


### 3. Ejecutar Football Module

Program arguments: https://api.football-data.org/v4/competitions/PD/matches?status=FINISHED <API_KEY> football.db tcp://localhost:61616

### 4. Ejecutar Weather Module

Program arguments: https://api.openweathermap.org/data/2.5/weather?q=%s&appid=<API_KEY>&units=metric weather.db tcp://localhost:61616


### 5. Ejecutar Business Unit

Program arguments: tcp://localhost:61616 C:\Users\gabri\IdeaProjects\DacdTrabajo\eventstore datamart.db

## Cómo Probar la Interfaz

La API REST está disponible en `http://localhost:7070`

### Endpoints disponibles

| Endpoint | Descripción |
|----------|-------------|
| `GET /partidos` | Todos los partidos con datos meteorológicos |
| `GET /partidos/{city}` | Partidos jugados en una ciudad concreta |
| `GET /partidos/equipo/{team}` | Partidos de un equipo concreto |
| `GET /tiempo/{city}` | Tiempo actual de una ciudad |
| `GET /alertas/lluvia` | Partidos jugados bajo la lluvia |

## Tecnologías Utilizadas

- **Java 21**
- **Maven** — gestión de dependencias
- **ActiveMQ 6.2.4** — broker de mensajería
- **Javalin 5.6.3** — API REST
- **SQLite** — persistencia del datamart
- **Gson** — serialización/deserialización JSON
- **football-data.org** — API de fútbol
- **OpenWeatherMap** — API meteorológica




Consola web disponible en: `http://127.0.0.1:8161` (usuario: `admin`, contraseña: `admin`)

### 2. Ejecutar Event Store Builder
