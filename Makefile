local-env-create:
	docker-compose -f stack.yaml up -d
	docker-compose -f stack.yaml build cardholderapp
	sleep 3
	docker cp data/ddl.sql postgres-cardholder:/var/lib/postgresql/data
	docker exec postgres-cardholder psql -h localhost -U admin -d postgres -a -f ./var/lib/postgresql/data/ddl.sql

local-env-destroy:
	docker-compose -f stack.yaml down