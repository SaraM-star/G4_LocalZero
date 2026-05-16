const initiativeForm = document.getElementById("initiativeForm");
const initiativesContainer = document.getElementById("initiatives");

let currentUser = null;

async function loadCurrentUser() {
    const response = await fetch("/api/initiatives/current-user");

    if (response.ok) {
        currentUser = await response.json();
    }
}

async function loadInitiatives() {
    initiativesContainer.innerHTML = "";

    const response = await fetch("/api/initiatives");
    const initiatives = await response.json();

    initiatives.forEach(initiative => {
        const card = document.createElement("div");
        card.className = "initiative-card";

        const participants = initiative.participants || [];
        const creator = initiative.creator;
        const isCreator = currentUser && creator && currentUser.id === creator.id;
        const hasJoined = participants.some(user => currentUser && user.id === currentUser.id);

        const participantsRows = participants.map(user => `
            <div class="participant-row">
                <span>${user.name || "Unknown"}</span>
                <span>${user.location || user.email || "No location"}</span>
            </div>
        `).join("");

        card.innerHTML = `
            <div class="initiative-card-header">
                <h3>${initiative.title}</h3>

                ${isCreator ? `
                    <div class="creator-actions-top">
                        <button type="button" onclick='editInitiative(${JSON.stringify(initiative)})'>Edit</button>
                        <button type="button" class="delete-button" onclick="deleteInitiative(${initiative.id})">Delete</button>
                    </div>
                ` : ""}
            </div>

            <p>${initiative.description}</p>

            <div class="initiative-meta">
                <span>📍 ${initiative.location || "No location"}</span>
                <span>🏷 ${initiative.category || "No category"}</span>
                <span>⏱ ${initiative.duration || "No duration"}</span>
                <span>👁 ${initiative.visibility || "Public"}</span>
                <span>🏘 ${initiative.neighborhood || "All neighborhoods"}</span>
                <span>👤 Created by: ${creator ? creator.name : "Unknown"}</span>
            </div>

            ${hasJoined ? `
                <button class="joined-button" type="button" disabled>
                    Joined
                </button>
            ` : `
                <button class="join-button" type="button" onclick="joinInitiative(${initiative.id})">
                    Join Initiative
                </button>
            `}

            <details class="participants-dropdown">
                <summary>View participants (${participants.length})</summary>

                <div class="participants-table">
                    <div class="participant-header">
                        <span>Name</span>
                        <span>Location / Email</span>
                    </div>

                    ${participantsRows || "<p>No participants yet.</p>"}
                </div>
            </details>
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

    const response = await fetch("/api/initiatives", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(initiative)
    });

    if (!response.ok) {
        alert("Could not create initiative.");
        return;
    }

    initiativeForm.reset();
    await loadInitiatives();
});

async function joinInitiative(id) {
    const response = await fetch(`/api/initiatives/${id}/join`, {
        method: "POST"
    });

    if (!response.ok) {
        alert("Could not join initiative.");
        return;
    }

    await loadInitiatives();
}

async function deleteInitiative(id) {
    if (!confirm("Are you sure you want to delete this initiative?")) {
        return;
    }

    const response = await fetch(`/api/initiatives/${id}`, {
        method: "DELETE"
    });

    if (!response.ok) {
        alert("Only the creator can delete this initiative.");
        return;
    }

    await loadInitiatives();
}

function editInitiative(initiative) {

    const card = event.target.closest(".initiative-card");

    card.innerHTML = `
        <h3>Edit Initiative</h3>

        <form id="editForm">

            <input
                id="editTitle"
                value="${initiative.title || ""}"
                placeholder="Title"
            >

            <textarea
                id="editDescription"
                placeholder="Description"
            >${initiative.description || ""}</textarea>

            <input
                id="editLocation"
                value="${initiative.location || ""}"
                placeholder="Location"
            >

            <input
                id="editDuration"
                value="${initiative.duration || ""}"
                placeholder="Duration"
            >

            <input
                id="editCategory"
                value="${initiative.category || ""}"
                placeholder="Category"
            >

            <select id="editVisibility">

                <option value="Public"
                ${initiative.visibility==="Public" ? "selected" : ""}>
                Public
                </option>

                <option value="Neighborhood"
                ${initiative.visibility==="Neighborhood" ? "selected" : ""}>
                Neighborhood
                </option>

            </select>

            <div class="creator-actions-top">

                <button type="submit">
                    Save
                </button>

                <button
                    type="button"
                    onclick="loadInitiatives()"
                >
                    Cancel
                </button>

            </div>

        </form>
    `;

    document
        .getElementById("editForm")
        .addEventListener("submit", async function(e){

            e.preventDefault();

            const updatedInitiative = {

                title:
                document.getElementById("editTitle").value,

                description:
                document.getElementById("editDescription").value,

                location:
                document.getElementById("editLocation").value,

                duration:
                document.getElementById("editDuration").value,

                category:
                document.getElementById("editCategory").value,

                visibility:
                document.getElementById("editVisibility").value
            };

            const response = await fetch(
                `/api/initiatives/${initiative.id}`,
                {
                    method:"PUT",
                    headers:{
                        "Content-Type":"application/json"
                    },
                    body:JSON.stringify(updatedInitiative)
                }
            );

            if(!response.ok){

                alert("Could not update initiative");
                return;
            }

            loadInitiatives();

        });

}

async function init() {
    await loadCurrentUser();
    await loadInitiatives();
}

init();