"""
External API Mock Server for GymPass Sync Testing

This mock server provides a simplified representation of an external
gym management system for testing sync jobs.
"""

import json
from datetime import date
from pathlib import Path
from typing import Any

from fastapi import FastAPI
from fastapi.responses import JSONResponse

# Initialize FastAPI app
app = FastAPI(
    title="GymPass External API Mock",
    description="Mock external gym management system for sync testing",
    version="1.0.0"
)

# Global data storage
members_data: list[dict[str, Any]] = []
memberships_data: list[dict[str, Any]] = []


def load_json_file(filepath: str) -> list[dict[str, Any]]:
    """Load and parse a JSON file."""
    path = Path(__file__).parent / filepath
    with open(path, 'r', encoding='utf-8') as f:
        return json.load(f)


@app.on_event("startup")
async def startup_event():
    """Load seed data on application startup."""
    global members_data, memberships_data
    members_data = load_json_file('data/members.json')
    memberships_data = load_json_file('data/memberships.json')


@app.get("/api/v1/sync/today")
async def get_today_sync() -> JSONResponse:
    """
    Get members and memberships created today for sync.
    
    Returns:
        JSON response with current date, members, and memberships
    """
    today = date.today().isoformat()
    
    todays_members = [
        m for m in members_data
        if m.get("updated_at", "").startswith(today)
    ]
    
    todays_memberships = [
        m for m in memberships_data
        if m.get("updated_at", "").startswith(today)
    ]
    
    response_data = {
        "date": today,
        "members": todays_members,
        "memberships": todays_memberships
    }
    
    return JSONResponse(content=response_data)


@app.get("/health")
async def health_check():
    """Health check endpoint."""
    return {"status": "healthy", "service": "external-api-mock"}


if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=5000)
