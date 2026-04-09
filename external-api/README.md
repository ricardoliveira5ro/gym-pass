# External API Mock

Mock external gym management system for GymPass sync testing.

## Purpose

This mock API simulates an external gym management system, providing member and membership data for testing sync jobs in the main GymPass application without requiring a real external system connection.

## Quick Start

### Install Dependencies

```bash
pip install -r requirements.txt
```

### Run the Server

```bash
uvicorn main:app --reload --port 5000
```

The server will start on `http://localhost:5000`.

### Test the Endpoint

```bash
curl http://localhost:5000/api/v1/sync/today
```

Expected response:

```json
{
  "date": "2026-03-30",
  "members": [...],
  "memberships": [...]
}
```

## API Contract

### GET /api/v1/sync/today

Returns all members and memberships for the current date.

**Response Format:**

```json
{
  "date": "YYYY-MM-DD",
  "members": [
    {
      "id": "ext-001",
      "name": "John Doe",
      "email": "john@example.com",
      "phone": "+1234567890",
      "created_at": "2026-01-15T10:00:00Z",
      "updated_at": "2026-03-30T08:00:00Z"
    }
  ],
  "memberships": [
    {
      "id": "mem-001",
      "member_id": "ext-001",
      "status": "active",
      "tier": "unlimited",
      "start_date": "2026-01-15",
      "end_date": "2027-01-15",
      "created_at": "2026-01-15T10:00:00Z",
      "updated_at": "2026-03-30T08:00:00Z"
    }
  ]
}
```

### Membership Status Values

- `active` - Membership is currently valid
- `expired` - Membership has ended
- `cancelled` - Membership was cancelled before end date

### Membership Tiers

- `basic` - Basic gym access
- `premium` - Premium access with additional benefits
- `unlimited` - Unlimited access to all facilities

## Health Check

```bash
curl http://localhost:5000/health
```

## Integration with Main App

Set the following environment variable in your main application:

```
EXTERNAL_API_BASE_URL=http://localhost:5000
```

## Seed Data

The API loads seed data from:
- `data/members.json` - 8 sample members
- `data/memberships.json` - 8 sample memberships

The data includes a mix of:
- Active, expired, and cancelled memberships
- Different membership tiers (basic, premium, unlimited)

## Development

The server supports auto-reload during development:

```bash
uvicorn main:app --reload --port 5000
```

Changes to Python files will automatically restart the server.
