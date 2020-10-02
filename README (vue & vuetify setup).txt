Getting up and running with Vue and Vuetify

1.	Install node.js  https://nodejs.org/en/download/
2.	Install yarn  https://classic.yarnpkg.com/en/docs/install/#windows-stable 
3.	Install vue/CLI (reference: https://cli.vuejs.org/guide/installation.html)
	a.	Open command prompt
	b.	Enter “yarn global add @vue/cli” (https://cli.vuejs.org/guide/installation.html) 
4.	Install vue (reference: https://www.npmjs.com/package/create-vue-app) 
	a.	Open command prompt
	b.	Enter “yarn global add create-vue-app”
5.	Install vuetify (reference: https://www.sitepoint.com/get-started-vuetify/)
	a.	Open command prompt
	b.	Enter “npm install vuetify” 
6.	Retrieve/sync “PatientRegistrationApp” folder from Github
	a.	It has everything except for a “node_modules” folder 
	b.	To get “node_modules” (for the first time), follow these steps:
		i.	In the command prompt, navigate to the “PatientRegistrationApp” folder
		ii.	Type in “vue add vuetify”
		iii.	When prompted, select “Configure (advanced)” rather than the Default
		iv.	Proceed to select the following options:
			? Use a pre-made template? (will replace App.vue and HelloWorld.vue) --> No
			? Use custom theme? --> No
			? Use custom properties (CSS variables)? --> No
			? Select icon font --> Material Design Icons
			? Use fonts as a dependency (for Electron or offline)? --> No
			? Use a-la-carte components? --> Yes
			? Select local (Use arrow keys) --> English
7.	To build the application, type “yarn build” into the command prompt (ensure you are in the “PatientRegistrationApp” folder).
8.	When complete, enter “yarn serve”
9.	When complete, open http://localhost:8080/ in your browser.
