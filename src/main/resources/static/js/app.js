const initiativeForm = document.getElementById("initiativeForm");
const initiativesContainer = document.getElementById("initiatives");

async function loadInitiatives() {
    initiativesContainer.innerHTML = "";

    const response = await fetch("/api/initiatives");
    const initiatives = await response.json();

    initiatives.forEach(initiative => {
        const card = document.createElement("div");
        card.className = "initiative-card";

        card.innerHTML = `
            <h3>${initiative.title}</h3>
            <p>${initiative.description}</p>

            <div class="initiative-meta">
                <span>📍 ${initiative.location || "No location"}</span>
                <span>🏷 ${initiative.category || "No category"}</span>
                <span>⏱ ${initiative.duration || "No duration"}</span>
                <span>👁 ${initiative.visibility || "Public"}</span>
            </div>

            <button class="join-button" type="button">
                Join Initiative
            </button>
        `;

        initiativesContainer.appendChild(card);
    });
}

initiativeForm.addEventListener("submit", async function (event) {
    event.preventDefault();

    const initiative = {
        title: document.getElementById("title").value,
        description: document.getElementById("description").value,
        location: document.getElementById("location").value,
        duration: document.getElementById("duration").value,
        category: document.getElementById("category").value,
        visibility: document.getElementById("visibility").value
    };

    await fetch("/api/initiatives", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(initiative)
    });

    initiativeForm.reset();
    await loadInitiatives();
});

loadInitiatives();