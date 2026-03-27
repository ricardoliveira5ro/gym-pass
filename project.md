# GymPass

GymPass is a comprehensive gym access control and analytics platform that bridges the gap between gym owners and their members through seamless digital entry and powerful insights.

## Problem Statement

Gym owners often use third-party software to manage their members, but lack centralized access control and unified analytics. Members need a simple way to access the gym without carrying physical cards, while owners need visibility into facility usage to optimize operations.

GymPass solves this by acting as a middleware layer—synchronizing data from external gym management systems and providing both access control and actionable insights.

## Architecture Overview

GymPass operates as a middleware solution that integrates with existing gym management systems. Rather than controlling the client database directly, GymPass synchronizes data from multiple external sources to provide unified access control and analytics.

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│  Client Gym     │     │    GymPass      │     │   End Users     │
│  Management     │────▶│   (Middleware)  │────▶│ (Members/Owners)
│  Systems        │     │                 │     │                 │
└─────────────────┘     └─────────────────┘     └─────────────────┘
        │                       │                        │
        │   Data Sync           │   Access Control       │
        │   (Polling Jobs)      │   (QR Codes)           │
        │                       │   (Insights API)       │
        └───────────────────────┴────────────────────────┘
```

## User Roles

| Role | Description |
|------|-------------|
| **Super Admin** | Platform administrator managing multiple gyms |
| **Gym Owner** | Owner of a specific gym, views owner insights |
| **Member** | Gym member who needs access and views personal insights |
| **System** | Automated processes (sync jobs, QR validation) |

## Core Features

### 1. Access Control

The access control feature enables frictionless gym entry through digital authentication:

- **User Authentication**: Secure login and registration system for gym members
  - Email/password authentication
  - JWT-based session management
  - Optional OAuth integration (future)

- **Permission Management**: Role-based access control to verify member eligibility
  - Membership status verification (active, expired, suspended)
  - Access tier validation (single entry, unlimited, peak hours only)
  - Guest pass support

- **QR Code Generation**: Dynamic QR codes that grant gym access
  - Time-limited validity (configurable per gym)
  - Single-use or multi-use options
  - Signed/encrypted to prevent forgery

- **Real-time Validation**: Instant verification at gym entry points
  - QR code scanning API for entry devices
  - Blacklist handling for banned members
  - Entry logging for audit trail

### 2. Insights and Reporting

Comprehensive analytics for both gym members and owners:

#### Client Insights

| Metric | Description |
|--------|-------------|
| **Peak Hour** | Visualization of busiest times to help members plan their visits |
| **% Affluence** | Current gym occupancy percentage |
| **Times This Week** | Number of visits within the current week |
| **Times This Month** | Number of visits within the current month |
| **Average** | Typical visit frequency (weekly/monthly) |
| **Streak** | Consecutive workout days to motivate members |

#### Owner Insights

| Metric | Description |
|--------|-------------|
| **Daily Entries** | Real-time count of gym entries today |
| **Peak Hour** | Identification of busiest hours for staffing decisions |
| **Active Members** | Count of members who visited in the last 30 days |
| **Tier List** | Member engagement ranking by visit frequency |
| **Average** | Typical daily/weekly attendance metrics |
| **Comparison with Past Months** | Month-over-month trend analysis |
| **Comparison with Past Quarters** | Quarter-over-quarter performance tracking |

## Data Synchronization

GymPass integrates with external gym management systems without controlling their data. The synchronization strategy ensures data consistency while respecting the external nature of client databases.

### Sync Strategy

```
┌─────────────────────────────────────────────────────────────┐
│                    Sync Architecture                        │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│         External DB                    GymPass DB           │
│        ┌────────────┐                  ┌──────────┐         │
│        │  Members   │◄───── Poll ──────│ Members  │         │
│        │  Access    │     (Partial)    │ Access   │         │
│        │  Logs      │                  │ Logs     │         │
│        │ Membership │                  │ Insights │         │
│        └────────────┘                  └──────────┘         │
│              │                              │               │
│              │       ┌──────────────┐       │               │
│              └──────►│  Full Scan   │◄──────┘               │
│                      │  (Daily Job) │                       │
│                      └──────────────┘                       │
└─────────────────────────────────────────────────────────────┘
```

- **Periodic Polling Job**: Scheduled background jobs that fetch incremental updates
  - Frequency: Configurable (default: every 15 minutes)
  - Fetches: New members, membership changes, access logs
  - Uses timestamps or change tracking from external source

- **Partial Updates**: Efficient delta synchronization
  - Only fetches records modified since last sync
  - Minimizes load on external systems
  - Updates local cache in Redis for fast access

- **Daily Full Scan**: End-of-day comprehensive scan
  - Ensures complete data accuracy
  - Catches any missed updates from polling failures
  - Regenerates aggregated insights
  - Scheduled during low-traffic hours

### Authentication Flow

```
1. Member registers/login → JWT token issued
2. Member requests QR code → Validates membership status
3. QR code generated → Signed with gym-specific key
4. At gym entrance → QR scanned, validated against API
5. Entry granted/denied → Logged for analytics
```