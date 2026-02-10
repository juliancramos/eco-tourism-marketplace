# Jenkins Pipeline Configuration

Post-deployment configuration for Jenkins pipeline.

## Environment Variables

Create `.env` file in Jenkins workspace:

```bash
docker exec -it jenkins bash
cd /var/jenkins_home/workspace/marketplace-pipeline

cat > .env << 'EOF'
.enf file content
EOF

exit
```

## Database Initialization

Verify schema after first build:

```bash
docker exec -it marketplace-db bash
psql -U admin -d marketplace-db
\dt
\q
exit
```

If schema not initialized, run manually:

```bash
docker exec -it marketplace-db bash
psql -U admin -d marketplace-db
\i /docker-entrypoint-initdb.d/01_schema.sql
\dt
\q
exit
```
