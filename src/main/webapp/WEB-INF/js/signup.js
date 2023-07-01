import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const Signup = () => {
  const [name, setName] = useState("");
  const [phoneno, setPhoneNO] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleSignup = (e) => {
    e.preventDefault();
    navigate("/login");
  };

  return (
    <div style={{ textAlign: "center" }}>
      <h2 style={{ color: "lightcyan", fontSize: "2rem" }}>Sign Up</h2>
      <form onSubmit={handleSignup}>
        <input
          style={{ padding: "10px", margin: "2%", border: "1px solid black" }}
          type="name"
          placeholder="Name"
          value={name}
          onChange={(e) => setName(e.target.value)}
        />
        <br />
        <input
          style={{ padding: "10px", margin: "2%", border: "1px solid black" }}
          type="number"
          placeholder="Phone No"
          value={phoneno}
          onChange={(e) => setPhoneNO(e.target.value)}
        />{" "}
        <br />
        <input
          style={{ padding: "10px", margin: "2%", border: "1px solid black" }}
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        <br />
        <input
          style={{ padding: "10px", margin: "1%", border: "1px solid black" }}
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <br />
        <button
          style={{
            backgroundColor: "forestgreen",
            color: "white",
            fontSize: "1.6rem",
            padding: "10px",
            border: "none",
            borderRadius: "5px",
          }}
          type="submit"
        >
          Sign Up
        </button>
        <br />
        <br />
        <button
          style={{
            backgroundColor: "forestgreen",
            color: "white",
            fontSize: "1.6rem",
            padding: "10px",
            border: "none",
            borderRadius: "5px",
          }}
          type="submit"
        >
          Login
        </button>
      </form>
    </div>
  );
};

export default Signup;