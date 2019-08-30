app_name=ester

build_dir=build
target_dir=$(build_dir)/$(app_name)
dot_env=.env
dot_env_example=.env.example

build: clean
	mvn -Dmaven.test.skip=true package
	mkdir -p $(target_dir)
	cp ./nginx.conf $(target_dir)/nginx.conf
	cp ./docker-compose.yml $(target_dir)/docker-compose.yml
	cp target/*.war $(target_dir)/app.war
	cp $(dot_env) $(target_dir)/$(dot_env) || cp $(dot_env_example) $(target_dir)/$(dot_env) && echo "Using $(dot_env_example) as $(dot_env)"

clean:
	rm -rf $(target_dir)
	mvn clean
