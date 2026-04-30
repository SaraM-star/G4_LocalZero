
fetch("/api/initiatives")
    .then(response => response.json())
    .then(data => {
        const list = document.getElementById("initiatives");

        data.forEach(initiative => {
            const li = document.createElement("li");
            li.textContent = initiative.title;
            list.appendChild(li);
        });
    });

