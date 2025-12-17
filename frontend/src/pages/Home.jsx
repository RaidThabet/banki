import React from "react";
import Navbar from "../components/Navbar";
import Footer from "../components/Footer"

export default function Home() {
  return (
    <>
      <Navbar />

      <div className="min-h-screen bg-gray-100 pt-32 px-4 md:px-8 lg:px-32">
        <div
          className="relative bg-white rounded-3xl p-10 md:p-16 shadow-xl
                     overflow-hidden"
          style={{ boxShadow: "0 0 40px 4px #00C16D" }}
        >
          <div className="grid md:grid-cols-2 gap-16 items-center">
            <div>
              <h1 className="text-4xl md:text-5xl font-extrabold text-gray-900 leading-tight">
                Your Smart <br />
                Banking Assistant
              </h1>

              <p className="mt-6 text-gray-600 leading-relaxed">
                <span className="font-semibold text-green-700">Banki</span> helps you
                manage, analyze, and understand your banking data securely and
                effortlessly — all in one place.
              </p>

              <p className="mt-3 text-gray-600">
                From checking balances to tracking transactions and managing
                accounts, Banki keeps everything simple and clear.
              </p>
            </div>

            <div className="flex justify-center">
              <div className="w-full max-w-md bg-gray-50 rounded-2xl p-6 shadow-inner">
                <p className="font-semibold text-gray-700 mb-4">
                  What you can do with Banki
                </p>

                <ul className="space-y-4">
                  <li className="flex items-center gap-3">
                    <span className="w-3 h-3 bg-green-600 rounded-full"></span>
                    <span>Check account balance in real time</span>
                  </li>
                  <li className="flex items-center gap-3">
                    <span className="w-3 h-3 bg-green-600 rounded-full"></span>
                    <span>Transfer money securely</span>
                  </li>
                  <li className="flex items-center gap-3">
                    <span className="w-3 h-3 bg-green-600 rounded-full"></span>
                    <span>View transaction history</span>
                  </li>
                  <li className="flex items-center gap-3">
                    <span className="w-3 h-3 bg-green-600 rounded-full"></span>
                    <span>Manage account details</span>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>

        {/* FEATURES SECTION */}
        <div className="mt-24 grid md:grid-cols-4 gap-8">
          {[
            {
              title: "Check Balance",
              desc: "Instant access to your account balance with visual indicators.",
            },
            {
              title: "Transfers",
              desc: "Secure and fast money transfers between accounts.",
            },
            {
              title: "Transaction History",
              desc: "Clear and organized view of all your transactions.",
            },
            {
              title: "Account Management",
              desc: "Update your personal and account information easily.",
            },
          ].map((item, index) => (
            <div
              key={index}
              className="bg-white rounded-2xl p-6 shadow hover:shadow-lg transition"
            >
              <h3 className="font-bold text-lg text-gray-800">
                {item.title}
              </h3>
              <p className="mt-2 text-gray-600 text-sm">
                {item.desc}
              </p>
            </div>
          ))}
        </div>

        {/* SECURITY SECTION */}
        <div className="mt-24 bg-white rounded-3xl p-10 md:p-16 shadow-xl">
          <h2 className="text-3xl font-extrabold text-gray-900">
            Security First
          </h2>
          <p className="mt-4 text-gray-600 max-w-3xl">
            Banki is built with security in mind. Your data is protected using
            modern authentication, controlled access, and secure backend
            services — ensuring confidentiality and reliability at every step.
          </p>
        </div>

        {/* CTA */}
        <div className="mt-24 text-center pb-24">
          <h2 className="text-3xl md:text-4xl font-extrabold text-gray-900">
            Ready to take control of your banking data?
          </h2>
          <p className="mt-4 text-gray-600">
            Get started now and experience a smarter way to manage your bank.
          </p>

          <button className="mt-8 bg-green-700 hover:bg-green-500 text-white font-semibold px-10 py-4 rounded-xl shadow-lg transition">
            Get Started
          </button>
        </div>
      </div>
      <Footer />
    </>
  );
}
